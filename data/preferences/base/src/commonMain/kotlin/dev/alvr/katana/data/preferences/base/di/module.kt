package dev.alvr.katana.data.preferences.base.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun encryptionModule(): Module

val dataPreferencesBaseModule = module {
    includes(encryptionModule())
}
