package dev.alvr.katana.core.tests.ui

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import dev.alvr.katana.core.common.annotations.KatanaInternalApi
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.core.ui.viewmodel.UiEffect
import dev.alvr.katana.core.ui.viewmodel.UiIntent
import dev.alvr.katana.core.ui.viewmodel.UiState
import io.kotest.matchers.shouldBe
import kotlin.jvm.JvmInline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@OptIn(KatanaInternalApi::class)
suspend fun <S : UiState, E : UiEffect, I : UiIntent> KatanaViewModel<S, E, I>.test(
    initialState: S? = null,
    verifyInitialState: VerifyInitialState<*> = VerifyInitialState.Skip,
    finalizationType: FinalizationType = FinalizationType.Ensure,
    test: suspend TestKatanaBaseViewModelScope<S, E, I>.() -> Unit,
) {
    let { viewModel ->
        merge(uiState.initialState(initialState).map { ItemState<S, E>(it) }, effects.map { ItemEffect(it) }).test {
            when (verifyInitialState) {
                is VerifyInitialState.ItemState -> awaitItem() shouldBe verifyInitialState.state
                VerifyInitialState.Skip -> skipItems(1)
            }

            TestKatanaBaseViewModelScopeImpl(this, viewModel).test()

            when (finalizationType) {
                FinalizationType.Drop -> cancelAndIgnoreRemainingEvents()
                FinalizationType.Ensure -> ensureAllEventsConsumed()
            }
        }
    }
}

sealed interface TestKatanaBaseViewModelScope<S : UiState, E : UiEffect, I : UiIntent> : ReceiveTurbine<Item<S, E>> {
    val currentState: S

    fun intent(intent: I)

    suspend fun expectState(state: S.() -> S)

    suspend fun expectEffect(effect: E)
}

@OptIn(KatanaInternalApi::class)
private class TestKatanaBaseViewModelScopeImpl<S : UiState, E : UiEffect, I : UiIntent>(
    private val turbine: ReceiveTurbine<Item<S, E>>,
    private val viewModel: KatanaViewModel<S, E, I>,
) : TestKatanaBaseViewModelScope<S, E, I>, ReceiveTurbine<Item<S, E>> by turbine {
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

    private suspend fun awaitState(): S {
        val item = awaitItem()
        return (item as? ItemState)?.state ?: error("Expected State but got $item")
    }

    private suspend fun awaitEffect(): E {
        val item = awaitItem()
        return (item as? ItemEffect)?.effect ?: error("Expected Effect but got $item")
    }
}

enum class FinalizationType {
    Drop,
    Ensure,
}

private fun <S : UiState> Flow<S>.initialState(initialState: S?): Flow<S> = flow {
    if (initialState != null) {
        drop(1)
        emit(initialState)
    }
    emitAll(this@initialState)
}

sealed interface VerifyInitialState<S : UiState> {
    data object Skip : VerifyInitialState<Nothing>

    @JvmInline value class ItemState<S : UiState>(val state: S) : VerifyInitialState<S>
}

sealed interface Item<S : UiState, E : UiEffect>

@JvmInline internal value class ItemState<S : UiState, E : UiEffect>(val state: S) : Item<S, E>

@JvmInline internal value class ItemEffect<S : UiState, E : UiEffect>(val effect: E) : Item<S, E>
