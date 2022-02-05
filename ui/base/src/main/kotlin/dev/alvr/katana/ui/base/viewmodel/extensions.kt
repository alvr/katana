package dev.alvr.katana.ui.base.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost

@Composable
@SuppressLint("ComposableNaming")
fun <S : Any, E : Any> ContainerHost<S, E>.collectSideEffect(
    effect: (suspend (sideEffect: E) -> Unit)
) {
    LaunchedEffect(this) {
        launch {
            container.sideEffectFlow.collect { effect(it) }
        }
    }
}

@Composable
fun <S : Any, E : Any> ContainerHost<S, E>.collectAsState(): State<S> =
    container.stateFlow.collectAsState()
