package dev.alvr.katana.ui.base.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlin.reflect.KProperty1
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost

@Composable
fun <STATE : Any, SIDE_EFFECT : Any, NEW_STATE : Any?> ContainerHost<STATE, SIDE_EFFECT>.collectAsState(
    prop: KProperty1<STATE, NEW_STATE>,
): State<NEW_STATE> {
    lateinit var initialValue: STATE

    val stateFlow = container.stateFlow
        .also { initialValue = it.value }
        .map { prop.get(it) }
        .distinctUntilChanged()

    val lifecycleOwner = LocalLifecycleOwner.current

    val stateFlowLifecycleAware = remember(stateFlow, lifecycleOwner) {
        stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    return stateFlowLifecycleAware.collectAsState(prop.get(initialValue))
}
