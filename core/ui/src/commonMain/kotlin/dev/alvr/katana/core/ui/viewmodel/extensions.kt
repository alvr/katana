package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.alvr.katana.core.common.annotations.KatanaInternalApi
import kotlinx.coroutines.launch

@Composable
@OptIn(KatanaInternalApi::class)
/**
     * Collects the view model's UI state as a lifecycle-aware Compose State.
     *
     * @return A `State<S>` representing the current UI state exposed by the view model.
     */
    fun <S : UiState, E : UiEffect, I : UiIntent> KatanaViewModel<S, E, I>.collectUiStateWithLifecycle() =
    uiState.collectAsStateWithLifecycle()

/**
 * Starts collecting the view model's `effects` when the lifecycle is at least `STARTED` and invokes
 * `onEffect` for each emitted `UiEffect` until the lifecycle stops or the composable is disposed.
 *
 * The most recent `onEffect` handler is used for delivery. The `onEffect` callback is invoked from
 * a suspend context and is annotated with `@DisallowComposableCalls`, so it must not call composable
 * functions.
 *
 * @param onEffect Handler called for every emitted `UiEffect`.
 */
@Composable
@OptIn(KatanaInternalApi::class)
fun <S : UiState, E : UiEffect, I : UiIntent> KatanaViewModel<S, E, I>.CollectEffect(
    onEffect: @DisallowComposableCalls suspend (E) -> Unit
) {
    val currentOnEffect by rememberUpdatedState(onEffect)

    LifecycleStartEffect(effects) {
        val job = lifecycleScope.launch { effects.collect { effect -> currentOnEffect(effect) } }

        onStopOrDispose { job.cancel() }
    }
}
