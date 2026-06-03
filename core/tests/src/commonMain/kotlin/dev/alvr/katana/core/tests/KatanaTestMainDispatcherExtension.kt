package dev.alvr.katana.core.tests

import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
object KatanaTestMainDispatcherExtension : BeforeSpecListener, AfterSpecListener {
    val scheduler = TestCoroutineScheduler()

    override suspend fun beforeSpec(spec: Spec) {
        Dispatchers.setMain(UnconfinedTestDispatcher(scheduler))
    }

    override suspend fun afterSpec(spec: Spec) {
        Dispatchers.resetMain()
    }
}
