package dev.alvr.katana.common.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import io.kotest.core.test.TestScope as KotestTestScope
import kotlinx.coroutines.test.TestScope as CoroutinesTestScope

@Suppress("UnusedReceiverParameter")
@OptIn(ExperimentalCoroutinesApi::class)
val KotestTestScope.orbitTestScope: CoroutinesTestScope
    get() = TestScope(UnconfinedTestDispatcher())
