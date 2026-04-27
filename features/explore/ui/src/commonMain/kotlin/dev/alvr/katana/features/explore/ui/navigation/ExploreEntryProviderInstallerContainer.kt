package dev.alvr.katana.features.explore.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaEntryProviderInstaller
import dev.alvr.katana.features.explore.ui.screens.explore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides

@BindingContainer
object ExploreEntryProviderInstallerContainer {

    @IntoSet @Provides fun provideExploreEntries(): KatanaEntryProviderInstaller = explore()
}
