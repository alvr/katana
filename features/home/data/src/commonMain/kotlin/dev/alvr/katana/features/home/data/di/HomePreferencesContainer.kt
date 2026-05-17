package dev.alvr.katana.features.home.data.di

import dev.alvr.katana.core.common.di.AppContext
import dev.alvr.katana.core.common.di.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Qualifier
import dev.zacsweers.metro.SingleIn
import eu.anifantakis.lib.ksafe.KSafe

@BindingContainer
@ContributesTo(AppScope::class)
expect object HomePreferencesContainer {

    @HomePreferences @SingleIn(AppScope::class) fun homePreferences(@AppContext context: PlatformContext): KSafe
}

@Qualifier internal annotation class HomePreferences

internal const val SafeFileName = "home"
