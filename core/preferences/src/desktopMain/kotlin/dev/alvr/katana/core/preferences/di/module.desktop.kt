package dev.alvr.katana.core.preferences.di

import dev.alvr.katana.core.preferences.encrypt.DesktopPreferencesEncrypt
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val securerModule = module {
    factoryOf(::DesktopPreferencesEncrypt) bind PreferencesEncrypt::class
}

internal actual fun encryptionModule() = module {
    includes(securerModule)
}
