package dev.alvr.katana.features.home.data.di

import dev.alvr.katana.core.common.KatanaFilesPath
import dev.alvr.katana.core.preferences.di.store.KatanaStore
import dev.alvr.katana.core.preferences.di.store.katanaStoreOf
import dev.alvr.katana.features.home.data.entities.HomePreferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface KatanaStoreHomePreferencesProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun katanaStoreHomePrefsProviders(path: KatanaFilesPath): KatanaStore<HomePreferences> =
        katanaStoreOf(path = path, name = "home", default = HomePreferences())
}
