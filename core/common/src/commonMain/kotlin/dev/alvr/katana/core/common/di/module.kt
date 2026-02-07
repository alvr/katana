package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.coroutines.AppKatanaDispatcher
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect fun katanaPathModule(): Module

private val dispatcherModule = module { singleOf(::AppKatanaDispatcher) bind KatanaDispatcher::class }

val coreCommonModule = module { includes(dispatcherModule, katanaPathModule()) }
