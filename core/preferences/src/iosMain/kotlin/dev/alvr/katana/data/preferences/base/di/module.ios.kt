package dev.alvr.katana.data.preferences.base.di

import dev.alvr.katana.data.preferences.base.encrypt.IosPreferencesEncrypt
import dev.alvr.katana.data.preferences.base.encrypt.PreferencesEncrypt
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val securerModule = module {
    factoryOf(::IosPreferencesEncrypt).bind<PreferencesEncrypt>()
}

internal actual fun encryptionModule() = module {
    includes(securerModule)
}
