package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl

expect abstract class BaseViewModel<S : Any, E : Any>() : ContainerHost<S, E> {
    protected val coroutineScope: CoroutineScope

    @OrbitDsl
    protected fun updateState(block: S.() -> S)

    protected fun onCleared()
}

@Composable
expect fun <S : Any, E : Any> ContainerHost<S, E>.collectAsState(): State<S>
