package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.Syntax

interface UiState

interface UiEffect

data object EmptyState : UiState

class EmptyEffect private constructor() : UiEffect

abstract class BaseViewModel<S : UiState, E : UiEffect> : ViewModel(), ContainerHost<S, E> {
    @OrbitDsl
    protected fun updateState(block: S.() -> S) {
        intent {
            reduce { block(state) }
        }
    }

    @OrbitDsl
    @OptIn(OrbitExperimental::class)
    @Suppress("UnusedReceiverParameter")
    protected suspend fun Syntax<S, E>.updateState(block: S.() -> S) {
        subIntent {
            reduce { block(state) }
        }
    }
}

@Composable
fun <S : UiState, E : UiEffect> BaseViewModel<S, E>.collectAsState(): State<S> =
    container.refCountStateFlow.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current,
    )

@Composable
@Suppress("ComposableFunctionName", "LambdaParameterInRestartableEffect")
fun <S : UiState, E : UiEffect> BaseViewModel<S, E>.collectEffect(onEffect: suspend (E) -> Unit) {
    val sideEffectFlow = container.refCountSideEffectFlow
    val lifecycleOwner = LocalLifecycleOwner.current

    val effect by rememberUpdatedState(onEffect)

    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sideEffectFlow.collect { effect ->
                effect(effect)
            }
        }
    }
}
