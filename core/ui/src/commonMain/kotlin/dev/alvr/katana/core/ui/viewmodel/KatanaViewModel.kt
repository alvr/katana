package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dev.alvr.katana.core.common.annotations.KatanaInternalApi
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.common.onSubscribe
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.FlowOptionUseCase
import dev.alvr.katana.core.domain.usecases.OptionUseCase
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@DslMarker internal annotation class KatanaViewModelDsl

@DslMarker internal annotation class KatanaViewModelExecuteDsl

@Stable
abstract class KatanaViewModel<S : UiState, E : UiEffect, I : UiIntent>(initialState: S) : ViewModel(), KoinComponent {
    internal val dispatcher by inject<KatanaDispatcher>()

    private val initialized = atomic(false)
    private val mutex = Mutex()

    private val _uiState = MutableStateFlow(initialState)
    private val _effects = Channel<E>()
    private val intents = Channel<I>()

    private val viewModelLogTag
        get() = this::class.simpleName ?: LogTag

    @KatanaInternalApi
    val uiState: StateFlow<S> =
        _uiState
            .onSubscribe(::onCreate)
            .stateIn(
                scope = viewModelScope + dispatcher.coroutineContext,
                started = SharingStarted.WhileSubscribed(SubscriptionDuration),
                initialValue = initialState,
            )

    @OptIn(KatanaInternalApi::class)
    protected val currentState
        get() = uiState.value

    @KatanaInternalApi val effects: Flow<E> = _effects.receiveAsFlow().onSubscribe(::onCreate).flowOn(dispatcher.main)

    private fun onCreate() {
        if (initialized.compareAndSet(expect = false, update = true)) {
            collectIntents()
            init()
        }
    }

    @KatanaViewModelDsl
    protected fun state(state: S.() -> S) {
        viewModelScope.launch(dispatcher.main) {
            mutex.withLock {
                val prevState = currentState
                val newState = state(prevState)

                if (prevState != newState) {
                    _uiState.value = newState

                    Logger.d(viewModelLogTag) {
                        """
                            |UiState changed:
                            |  Previous: $prevState
                            |  New       $newState
                        """
                            .trimMargin()
                    }
                }
            }
        }
    }

    @KatanaViewModelDsl
    protected fun effect(effect: E) {
        viewModelScope.launch(dispatcher.main) { _effects.send(effect) }
    }

    @KatanaViewModelDsl
    fun intent(intent: I) {
        viewModelScope.launch(dispatcher.main) { intents.send(intent) }
    }

    private fun collectIntents() {
        viewModelScope.launch(dispatcher.main) { intents.consumeAsFlow().collect { intent -> handleIntent(intent) } }
    }

    protected open fun handleIntent(intent: I) {
        // no-op
    }

    protected open fun init() {
        // no-op
    }

    @KatanaViewModelExecuteDsl
    protected fun <P, R> execute(
        useCase: EitherUseCase<P, R>,
        params: P,
        onSuccess: (R) -> Unit,
        onFailure: (Failure) -> Unit,
    ) {
        viewModelScope.launch(dispatcher.io) {
            val result = useCase(params)
            withContext(dispatcher.main) { result.fold(onFailure, onSuccess) }
        }
    }

    @KatanaViewModelExecuteDsl
    protected fun <P, R> execute(useCase: OptionUseCase<P, R>, params: P, onSome: (R) -> Unit, onEmpty: () -> Unit) {
        viewModelScope.launch(dispatcher.io) {
            val result = useCase(params)
            withContext(dispatcher.main) { result.fold(onEmpty, onSome) }
        }
    }

    @KatanaViewModelExecuteDsl
    protected fun <P, R> execute(
        useCase: FlowEitherUseCase<P, R>,
        params: P,
        onSuccess: (R) -> Unit,
        onFailure: (Failure) -> Unit,
    ) {
        viewModelScope.launch(dispatcher.io) {
            useCase(params)
            useCase.flow.collect { result -> withContext(dispatcher.main) { result.fold(onFailure, onSuccess) } }
        }
    }

    @KatanaViewModelExecuteDsl
    protected fun <P, R> execute(
        useCase: FlowOptionUseCase<P, R>,
        params: P,
        onSome: (R) -> Unit,
        onEmpty: () -> Unit,
    ) {
        viewModelScope.launch(dispatcher.io) {
            useCase(params)
            useCase.flow.collect { result -> withContext(dispatcher.main) { result.fold(onEmpty, onSome) } }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancel()
        dispatcher.cancel()
    }
}

private const val LogTag = "KatanaBaseViewModel"
private const val SubscriptionDuration = 2_500L
