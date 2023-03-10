package dev.alvr.katana.ui.base.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

abstract class BaseViewModel<S : Any, E : Any> : ViewModel(), ContainerHost<S, E> {

    @OrbitDsl
    protected fun updateState(block: S.() -> S) {
        intent {
            reduce { block(state) }
        }
    }
}

@Composable
fun <S : Any, E : Any> ContainerHost<E, S>.collectAsState() = container.stateFlow.collectAsStateWithLifecycle()
