package dev.alvr.katana.ui.base.viewmodel

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.OrbitDsl
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
