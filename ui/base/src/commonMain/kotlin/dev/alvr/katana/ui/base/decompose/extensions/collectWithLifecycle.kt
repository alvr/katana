package dev.alvr.katana.ui.base.decompose.extensions

import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.value.ObserveLifecycleMode
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

context(LifecycleOwner)
fun <T : Any> Flow<T>.collectWithLifecycle(
    mode: ObserveLifecycleMode = ObserveLifecycleMode.START_STOP,
    collector: FlowCollector<T>,
) {
    val scope = MainScope() + SupervisorJob()
    var job: Job? = null

    when (mode) {
        ObserveLifecycleMode.CREATE_DESTROY ->
            lifecycle.subscribe(
                onCreate = { job = scope.launch { collect(collector) } },
                onDestroy = { job?.cancel() },
            )

        ObserveLifecycleMode.START_STOP ->
            lifecycle.subscribe(
                onStart = { job = scope.launch { collect(collector) } },
                onStop = { job?.cancel() },
            )

        ObserveLifecycleMode.RESUME_PAUSE ->
            lifecycle.subscribe(
                onResume = { job = scope.launch { collect(collector) } },
                onPause = { job?.cancel() },
            )
    }.let {}
}
