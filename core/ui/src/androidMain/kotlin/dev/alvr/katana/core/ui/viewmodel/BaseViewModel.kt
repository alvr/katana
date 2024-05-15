package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

actual abstract class BaseViewModel<S : Any, E : Any> : ViewModel(), ContainerHost<S, E> {
    protected actual val coroutineScope = viewModelScope

    @OrbitDsl
    protected actual fun updateState(block: S.() -> S) {
        intent {
            reduce { block(state) }
        }
    }

    actual override fun onCleared() {
        super.onCleared()
    }
}

@Composable
actual fun <S : Any, E : Any> ContainerHost<S, E>.collectAsState() =
    container.stateFlow.collectAsStateWithLifecycle(LocalLifecycleOwner.current)
