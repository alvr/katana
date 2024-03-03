package dev.alvr.katana.core.preferences.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun encryptionModule(): Module

val dataPreferencesBaseModule = module {
    includes(encryptionModule())
}
