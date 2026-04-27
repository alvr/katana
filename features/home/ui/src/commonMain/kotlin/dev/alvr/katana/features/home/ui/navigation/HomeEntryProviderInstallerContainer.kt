package dev.alvr.katana.features.home.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaEntryProviderInstaller
import dev.alvr.katana.features.home.ui.screens.home
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides

@BindingContainer
object HomeEntryProviderInstallerContainer {

    @IntoSet @Provides fun provideHomeEntries(): KatanaEntryProviderInstaller = home()
}
