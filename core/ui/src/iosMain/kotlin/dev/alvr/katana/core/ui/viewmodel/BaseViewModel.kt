package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

actual abstract class BaseViewModel<S : Any, E : Any> : ContainerHost<S, E> {
    protected actual val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @OrbitDsl
    protected actual fun updateState(block: S.() -> S) {
        intent {
            reduce { block(state) }
        }
    }

    protected actual fun onCleared() {
        coroutineScope.cancel()
    }
}

@Composable
actual fun <S : Any, E : Any> ContainerHost<S, E>.collectAsState() =
    container.stateFlow.collectAsState()
