package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreCommonModule = module {
    singleOf(::KatanaDispatcher)
}
