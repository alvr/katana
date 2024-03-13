package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.coroutines.AppKatanaDispatcher
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val dispatcherModule = module {
    singleOf(::AppKatanaDispatcher) bind KatanaDispatcher::class
}

val coreCommonModule = module {
    includes(dispatcherModule)
}
