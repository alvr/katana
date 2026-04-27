package dev.alvr.katana.features.account.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaEntryProviderInstaller
import dev.alvr.katana.features.account.ui.screens.account
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides

@BindingContainer
object AccountEntryProviderInstallerContainer {

    @IntoSet @Provides fun provideAccountEntries(): KatanaEntryProviderInstaller = account()
}
