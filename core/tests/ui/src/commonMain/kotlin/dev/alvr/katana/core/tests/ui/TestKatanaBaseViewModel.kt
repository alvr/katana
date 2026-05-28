package dev.alvr.katana.core.tests.ui

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.turbineScope
import dev.alvr.katana.core.common.annotations.KatanaInternalApi
import dev.alvr.katana.core.tests.KatanaTestMainDispatcherExtension
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.core.ui.viewmodel.UiEffect
import dev.alvr.katana.core.ui.viewmodel.UiIntent
import dev.alvr.katana.core.ui.viewmodel.UiState
import io.kotest.matchers.shouldBe
import kotlin.jvm.JvmInline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.test.TestCoroutineScheduler

@OptIn(KatanaInternalApi::class)
suspend fun <S : UiState, E : UiEffect, I : UiIntent> KatanaViewModel<S, E, I>.test(
    finalizationType: FinalizationType = FinalizationType.Ensure,
    test: suspend TestKatanaBaseViewModelScope<S, E, I>.() -> Unit,
) = turbineScope {
    val scheduler = KatanaTestMainDispatcherExtension.scheduler

    val uiState = uiState.map { state -> ItemState<S, E>(state) }
    val effects = effects.map { effect -> ItemEffect<S, E>(effect) }

    val testScope = CoroutineScope(currentCoroutineContext() + Dispatchers.Main)
    val turbine = merge(uiState, effects).testIn(testScope)
    val scope = TestKatanaBaseViewModelScopeImpl(turbine = turbine, viewModel = this@test, scheduler = scheduler)

    scope.skipInitialState()
    scope.test()

    scheduler.advanceUntilIdle()

    when (finalizationType) {
        FinalizationType.Drop -> turbine.cancelAndIgnoreRemainingEvents()
        FinalizationType.Ensure -> {
            scope.ensureNoPendingEvents()
            turbine.ensureAllEventsConsumed()
            turbine.cancel()
        }
    }
}

sealed interface TestKatanaBaseViewModelScope<S : UiState, E : UiEffect, I : UiIntent> {
    val currentState: S

    fun intent(intent: I)

    suspend fun expectState(state: S.() -> S)

    suspend fun expectEffect(effect: E)
}

@OptIn(KatanaInternalApi::class)
private class TestKatanaBaseViewModelScopeImpl<S : UiState, E : UiEffect, I : UiIntent>(
    private val turbine: ReceiveTurbine<Item<S, E>>,
    private val viewModel: KatanaViewModel<S, E, I>,
    private val scheduler: TestCoroutineScheduler,
) : TestKatanaBaseViewModelScope<S, E, I> {
    private val pendingStates = ArrayDeque<S>()
    private val pendingEffects = ArrayDeque<E>()

    override val currentState: S
        get() = viewModel.uiState.value

    override fun intent(intent: I) {
        viewModel.intent(intent)
    }

    override suspend fun expectState(state: S.() -> S) {
        with(awaitState()) { this shouldBe state() }
    }

    override suspend fun expectEffect(effect: E) {
        awaitEffect() shouldBe effect
    }

    suspend fun skipInitialState() {
        awaitState()
    }

    fun ensureNoPendingEvents() {
        check(pendingStates.isEmpty() && pendingEffects.isEmpty()) {
            "Unconsumed pending events: states=$pendingStates, effects=$pendingEffects"
        }
    }

    private suspend fun awaitState(): S {
        pendingStates.removeFirstOrNull()?.let { state ->
            return state
        }

        while (true) {
            scheduler.advanceUntilIdle()
            when (val item = turbine.awaitItem()) {
                is ItemState -> return item.state
                is ItemEffect -> pendingEffects.addLast(item.effect)
            }
        }
    }

    private suspend fun awaitEffect(): E {
        pendingEffects.removeFirstOrNull()?.let { effect ->
            return effect
        }

        while (true) {
            scheduler.advanceUntilIdle()
            when (val item = turbine.awaitItem()) {
                is ItemEffect -> return item.effect
                is ItemState -> pendingStates.addLast(item.state)
            }
        }
    }
}

enum class FinalizationType {
    Drop,
    Ensure,
}

sealed interface Item<S : UiState, E : UiEffect>

@JvmInline private value class ItemState<S : UiState, E : UiEffect>(val state: S) : Item<S, E>

@JvmInline private value class ItemEffect<S : UiState, E : UiEffect>(val effect: E) : Item<S, E>
