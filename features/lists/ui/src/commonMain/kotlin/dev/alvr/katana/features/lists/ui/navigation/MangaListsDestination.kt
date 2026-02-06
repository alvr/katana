package dev.alvr.katana.features.lists.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaDestination
import kotlinx.serialization.Serializable

sealed interface MangaListsDestination : KatanaDestination {

    @Serializable data object Root : MangaListsDestination

    @Serializable data object Lists : MangaListsDestination
}
