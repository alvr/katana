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

    private fun onCreate() {
        if (initialized.compareAndSet(expect = false, update = true)) {
            init()
        }
    }

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

    protected fun effect(effect: E) {
        viewModelScope.launch { _effects.send(effect) }
    }

    open fun intent(intent: I) {
        // no-op
    }

    protected open fun init() {
        // no-op
    }

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
