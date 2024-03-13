package dev.alvr.katana.core.tests.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.coroutines.TestKatanaDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val dispatcherModule = module {
    singleOf(::TestKatanaDispatcher) bind KatanaDispatcher::class
}

val coreTestsModule = module {
    includes(dispatcherModule)
}
