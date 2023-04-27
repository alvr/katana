package dev.alvr.katana.data.preferences.base.di

import dev.alvr.katana.data.preferences.base.securer.IosPreferencesSecurer
import dev.alvr.katana.data.preferences.base.securer.PreferencesSecurer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val securerModule = module {
    factoryOf(::IosPreferencesSecurer).bind<PreferencesSecurer>()
}

internal actual fun encryptionModule() = module {
    includes(securerModule)
}
