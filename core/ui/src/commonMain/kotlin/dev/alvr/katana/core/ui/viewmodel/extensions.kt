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
fun <S : UiState, E : UiEffect, I : UiIntent> KatanaViewModel<S, E, I>.collectAsState() =
    uiState.collectAsStateWithLifecycle(context = dispatcher.immediate)

@Composable
@OptIn(KatanaInternalApi::class)
fun <S : UiState, E : UiEffect, I : UiIntent> KatanaViewModel<S, E, I>.CollectEffect(
    onEffect: @DisallowComposableCalls suspend (E) -> Unit
) {
    val currentOnEffect by rememberUpdatedState(onEffect)

    LifecycleStartEffect(effects) {
        val job = lifecycleScope.launch(dispatcher.immediate) { effects.collect { effect -> currentOnEffect(effect) } }

        onStopOrDispose { job.cancel() }
    }
}
