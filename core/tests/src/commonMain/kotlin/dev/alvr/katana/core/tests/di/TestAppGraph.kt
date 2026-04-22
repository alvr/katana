package dev.alvr.katana.core.tests.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.coroutines.TestKatanaDispatcher
import dev.zacsweers.metro.Binds

abstract class TestAppScope private constructor()

interface TestAppGraph {
    val dispatcher: TestKatanaDispatcher

    @Binds val TestKatanaDispatcher.binds: KatanaDispatcher
}
