package dev.alvr.katana.features.lists.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaEntryProviderInstaller
import dev.alvr.katana.features.lists.ui.screens.animeLists
import dev.alvr.katana.features.lists.ui.screens.mangaLists
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides

@BindingContainer
object ListsEntryProviderInstallerContainer {

    @IntoSet
    @Provides
    fun provideListsEntries(): KatanaEntryProviderInstaller = {
        animeLists()
        mangaLists()
    }
}
