package dev.alvr.katana.ui.base.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import io.github.aakira.napier.Napier
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.OrbitDsl
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

abstract class BaseViewModel<S : Any, E : Any> : ViewModel() {

    private val host by lazy {
        object : ContainerHost<S, E> {
            override val container: Container<S, E> = this@BaseViewModel.container
        }
    }

    protected abstract val container: Container<S, E>

    private val tag get() = this::class.java.canonicalName

    @Composable
    @SuppressLint("ComposableNaming")
    fun collectSideEffect(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        effect: suspend (sideEffect: E) -> Unit
    ) {
        val sideEffectFlow = container.sideEffectFlow
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(sideEffectFlow, lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
                sideEffectFlow.collect { effect(it) }
            }
        }
    }

    @Composable
    fun collectAsState(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
    ): State<S> {
        val stateFlow = container.stateFlow
        val lifecycleOwner = LocalLifecycleOwner.current

        val stateFlowLifecycleAware = remember(stateFlow, lifecycleOwner) {
            stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
        }

        @SuppressLint("StateFlowValueCalledInComposition")
        val initialValue = stateFlow.value
        return stateFlowLifecycleAware.collectAsState(initialValue)
    }

    protected fun executeUseCase(
        execute: suspend SimpleSyntax<S, E>.() -> Unit,
        error: suspend SimpleSyntax<S, E>.(Throwable) -> Unit = {}
    ) {
        @Suppress("TooGenericExceptionCaught")
        host.intent {
            try {
                execute()
            } catch (error: Throwable) {
                Napier.e(throwable = error, tag = tag) { error.message.orEmpty() }
                error(error)
            }
        }
    }

    @OrbitDsl
    protected suspend fun SimpleSyntax<S, E>.updateState(update: S.() -> S) {
        reduce {
            state.update()
        }
    }

    @OrbitDsl
    protected fun updateState(update: S.() -> S) {
        host.intent {
            reduce {
                state.update()
            }
        }
    }

    @OrbitDsl
    protected fun postSideEffect(effect: E) {
        host.intent {
            postSideEffect(effect)
        }
    }
}
