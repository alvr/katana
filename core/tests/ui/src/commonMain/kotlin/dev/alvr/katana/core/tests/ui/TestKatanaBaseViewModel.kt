package dev.alvr.katana.core.tests.ui

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import dev.alvr.katana.core.common.annotations.KatanaInternalApi
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.core.ui.viewmodel.UiEffect
import dev.alvr.katana.core.ui.viewmodel.UiIntent
import dev.alvr.katana.core.ui.viewmodel.UiState
import io.kotest.matchers.shouldBe
import kotlin.jvm.JvmInline
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

/**
 * Executes a test against this ViewModel by merging its `uiState` and `effects` flows into a single test stream
 * and running the provided `test` block within a `TestKatanaBaseViewModelScope`.
 *
 * The merged stream yields tagged state and effect items; the scope buffers out-of-order arrivals so tests
 * can assert states and effects in logical order. The scope's initial ViewModel state is skipped before
 * invoking the `test` block.
 *
 * @receiver The ViewModel under test.
 * @param finalizationType Controls post-test behavior:
 *  - `FinalizationType.Drop`: cancels and ignores any remaining events.
 *  - `FinalizationType.Ensure`: verifies there are no pending buffered items in the scope and ensures all events were consumed.
 * @param test The test body executed with a `TestKatanaBaseViewModelScope` receiver for issuing intents and asserting states/effects.
 */
@OptIn(KatanaInternalApi::class)
suspend fun <S : UiState, E : UiEffect, I : UiIntent> KatanaViewModel<S, E, I>.test(
    finalizationType: FinalizationType = FinalizationType.Ensure,
    test: suspend TestKatanaBaseViewModelScope<S, E, I>.() -> Unit,
) {
    val uiState = uiState.map { state -> ItemState<S, E>(state) }
    val effects = effects.map { effect -> ItemEffect<S, E>(effect) }

    merge(uiState, effects).test {
        val scope = testViewModel()

        scope.skipInitialState()

        scope.test()

        when (finalizationType) {
            FinalizationType.Drop -> cancelAndIgnoreRemainingEvents()
            FinalizationType.Ensure -> {
                scope.ensureNoPendingEvents()
                ensureAllEventsConsumed()
            }
        }
    }
}

sealed interface TestKatanaBaseViewModelScope<S : UiState, E : UiEffect, I : UiIntent> {
    val currentState: S

    /**
 * Sends the given intent to the underlying view model for processing.
 *
 * @param intent The intent to dispatch to the view model.
 */
fun intent(intent: I)

    suspend fun expectState(state: S.() -> S)

    /**
 * Asserts that the next effect emitted by the ViewModel equals the given effect and consumes it.
 *
 * @param effect The expected effect to be received and asserted.
 */
suspend fun expectEffect(effect: E)
}

context(viewModel: KatanaViewModel<S, E, I>, turbine: TurbineTestContext<Item<S, E>>)
/**
     * Creates a TestKatanaBaseViewModelScopeImpl bound to the current turbine and view model.
     *
     * @return A TestKatanaBaseViewModelScopeImpl configured with the current `turbine` and `viewModel`.
     */
    private fun <S : UiState, E : UiEffect, I : UiIntent> testViewModel() =
    TestKatanaBaseViewModelScopeImpl(turbine = turbine, viewModel = viewModel)

@OptIn(KatanaInternalApi::class)
private class TestKatanaBaseViewModelScopeImpl<S : UiState, E : UiEffect, I : UiIntent>(
    private val turbine: ReceiveTurbine<Item<S, E>>,
    private val viewModel: KatanaViewModel<S, E, I>,
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

    /**
     * Asserts that the next emitted effect equals the provided expected effect.
     *
     * @param effect The expected effect to compare against the next received effect.
     * @throws AssertionError if the next effect does not equal `effect`.
     */
    override suspend fun expectEffect(effect: E) {
        awaitEffect() shouldBe effect
    }

    /**
     * Consumes and discards the next state event from the test stream.
     *
     * Use this to skip the ViewModel's initial `uiState` emission before making further expectations.
     */
    suspend fun skipInitialState() {
        awaitState()
    }

    /**
     * Asserts that there are no queued state or effect events.
     *
     * @throws IllegalStateException if any pending states or effects remain; the exception message lists remaining items.
     */
    fun ensureNoPendingEvents() {
        check(pendingStates.isEmpty() && pendingEffects.isEmpty()) {
            "Unconsumed pending events: states=$pendingStates, effects=$pendingEffects"
        }
    }

    /**
     * Waits for the next state event and returns it, buffering any intervening effect events.
     *
     * If a previously buffered state exists, that state is returned immediately; otherwise this
     * suspends until an `ItemState` is received. Any `ItemEffect` items observed while waiting
     * are appended to the pending effects queue.
     *
     * @return The next `S` state emitted (from the buffered states or from the turbine stream).
     */
    private suspend fun awaitState(): S {
        pendingStates.removeFirstOrNull()?.let {
            return it
        }

        while (true) {
            when (val item = turbine.awaitItem()) {
                is ItemState -> return item.state
                is ItemEffect -> pendingEffects.addLast(item.effect)
            }
        }
    }

    /**
     * Await the next emitted effect, buffering any intervening states for later consumption.
     *
     * If an effect is already queued, returns it immediately; otherwise suspends until an `ItemEffect`
     * is received from the turbine. Any `ItemState` values encountered while waiting are enqueued into
     * `pendingStates`.
     *
     * @return The next emitted effect.
     */
    private suspend fun awaitEffect(): E {
        pendingEffects.removeFirstOrNull()?.let {
            return it
        }

        while (true) {
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
