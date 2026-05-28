package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dev.alvr.katana.core.common.annotations.KatanaInternalApi
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowOptionUseCase
import dev.alvr.katana.core.domain.usecases.KatanaOptionUseCase
import dev.alvr.katana.core.domain.usecases.KatanaUseCase
import dev.zacsweers.metro.DefaultBinding
import dev.zacsweers.metro.ExperimentalMetroApi
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
@DefaultBinding<ViewModel>
@OptIn(ExperimentalMetroApi::class)
abstract class KatanaViewModel<S : UiState, E : UiEffect, I : UiIntent>(initialState: S) : ViewModel() {

    private val initialized = atomic(false)

    private val _uiState = MutableStateFlow(initialState)
    private val _effects = Channel<E>(Channel.BUFFERED)

    private val executingLock = SynchronizedObject()
    private val executing = mutableMapOf<Any, Job>()

    private val viewModelLogTag
        get() = this::class.simpleName ?: LogTag

    @KatanaInternalApi
    val uiState: StateFlow<S> =
        _uiState
            .onSubscription { onCreate() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SubscriptionDuration),
                initialValue = initialState,
            )

    @OptIn(KatanaInternalApi::class)
    protected val currentState
        get() = uiState.value

    @KatanaInternalApi val effects: Flow<E> = _effects.receiveAsFlow()

    /**
     * Invokes [init] on the first call; subsequent calls do nothing.
     */
    private fun onCreate() {
        if (initialized.compareAndSet(expect = false, update = true)) {
            init()
        }
    }

    /**
     * Applies a transformation to the current UI state and updates the view model's state flow with the result.
     *
     * @param state A receiver-style transformer invoked with the current `S` that produces the next `S` to be stored.
     */
    protected fun state(state: S.() -> S) {
        _uiState.update { prevState ->
            val newState = state(prevState)

            if (prevState != newState) {
                Logger.d(tag = viewModelLogTag) {
                    """
                        |UiState changed:
                        |  Previous: $prevState
                        |  New       $newState
                    """
                        .trimMargin()
                }
            }

            newState
        }
    }

    /**
     * Emits a one-off UI effect to be observed by the UI layer.
     *
     * @param effect The effect instance to emit.
     */
    protected fun effect(effect: E) {
        viewModelScope.launch { _effects.send(effect) }
    }

    /**
     * Handles an incoming UI intent.
     *
     * Subclasses override this to react to intents emitted by the UI.
     *
     * @param intent The UI intent to handle.
     */
    open fun intent(intent: I) {
        // no-op
    }

    /**
     * Called once when the ViewModel is first created (triggered on the first subscription to `uiState`).
     *
     * Default implementation does nothing; subclasses may override to perform one-time setup.
     */
    protected open fun init() {
        // no-op
    }

    /**
     * Executes the provided KatanaEitherUseCase with the given parameters and dispatches the outcome to callbacks.
     *
     * Starts a coroutine to run the use-case and, if a previous job for the same use-case instance is running, cancels it before starting the new one.
     *
     * @param useCase The use-case instance to execute.
     * @param params Parameters to pass to the use-case.
     * @param onSuccess Invoked with the success value when the use-case completes successfully.
     * @param onFailure Invoked with the failure when the use-case produces an error.
     */
    protected fun <P, R> execute(
        useCase: KatanaEitherUseCase<P, R>,
        params: P,
        onSuccess: (R) -> Unit,
        onFailure: (Failure) -> Unit,
    ) {
        useCase.execute {
            val result = useCase(params)
            result.fold(onFailure, onSuccess)
        }
    }

    /**
     * Executes the given `KatanaOptionUseCase` with `params` and invokes the appropriate callback
     * for an emitted value or an empty result.
     *
     * @param useCase The use-case instance to execute.
     * @param params Parameters to pass to the use-case.
     * @param onSome Called with the result when the use-case yields a value.
     * @param onEmpty Called when the use-case yields no value.
     */
    protected fun <P, R> execute(
        useCase: KatanaOptionUseCase<P, R>,
        params: P,
        onSome: (R) -> Unit,
        onEmpty: () -> Unit,
    ) {
        useCase.execute {
            val result = useCase(params)
            result.fold(onEmpty, onSome)
        }
    }

    /**
     * Executes a flow-based either use case, collecting its emitted results and invoking the provided callbacks.
     *
     * This starts a job for the given `useCase`, cancels any previously running job for the same use case instance,
     * collects values from `useCase.flow`, and for each emitted `Either` calls `onFailure` for failures or `onSuccess` for successes.
     *
     * @param useCase The flow-based either use case to execute.
     * @param params Parameters to pass to the use case when starting it.
     * @param onSuccess Called for each successful result emitted by the use case.
     * @param onFailure Called for each failure emitted by the use case.
     */
    protected fun <P, R> execute(
        useCase: KatanaFlowEitherUseCase<P, R>,
        params: P,
        onSuccess: (R) -> Unit,
        onFailure: (Failure) -> Unit,
    ) {
        useCase.execute {
            useCase(params)
            useCase.flow.collect { result -> result.fold(onFailure, onSuccess) }
        }
    }

    /**
     * Execute the given flow-based option use case and handle each emitted value or empty result.
     *
     * Invokes the use case with [params], collects its `flow`, calls [onSome] for every emitted value
     * and [onEmpty] for every emitted empty result. Starting this execution cancels any previously
     * running job for the same use-case instance.
     *
     * @param useCase The flow-producing option use case to execute.
     * @param params Parameters passed to the use case when invoked.
     * @param onSome Called for each non-empty result emitted by the use case.
     * @param onEmpty Called for each empty result emitted by the use case.
     */
    protected fun <P, R> execute(
        useCase: KatanaFlowOptionUseCase<P, R>,
        params: P,
        onSome: (R) -> Unit,
        onEmpty: () -> Unit,
    ) {
        useCase.execute {
            useCase(params)
            useCase.flow.collect { result -> result.fold(onEmpty, onSome) }
        }
    }

    /**
     * Schedules and runs a coroutine for this use-case, cancelling any previously running job for the same use-case instance.
     *
     * Creates a lazily-started coroutine in the ViewModel's scope, registers it in the internal executing map (replacing
     * and cancelling any existing job for this use-case), and starts the new job.
     *
     * @param block The suspendable work to execute inside the coroutine.
     */
    private inline fun KatanaUseCase<*, *>.execute(crossinline block: suspend () -> Unit) {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) { block() }
        synchronized(executingLock) {
            executing.remove(this)?.cancel()
            executing[this] = job
        }
        job.start()
    }
}

private const val LogTag = "KatanaBaseViewModel"
private const val SubscriptionDuration = 2_500L
