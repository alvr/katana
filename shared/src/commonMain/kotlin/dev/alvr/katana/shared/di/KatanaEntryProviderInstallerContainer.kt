package dev.alvr.katana.shared.di

import dev.alvr.katana.features.account.ui.navigation.AccountEntryProviderInstallerContainer
import dev.alvr.katana.features.explore.ui.navigation.ExploreEntryProviderInstallerContainer
import dev.alvr.katana.features.home.ui.navigation.HomeEntryProviderInstallerContainer
import dev.alvr.katana.features.lists.ui.navigation.ListsEntryProviderInstallerContainer
import dev.zacsweers.metro.BindingContainer

@BindingContainer(
    includes =
        [
            AccountEntryProviderInstallerContainer::class,
            ExploreEntryProviderInstallerContainer::class,
            HomeEntryProviderInstallerContainer::class,
            ListsEntryProviderInstallerContainer::class,
        ]
)
object KatanaEntryProviderInstallerContainer
