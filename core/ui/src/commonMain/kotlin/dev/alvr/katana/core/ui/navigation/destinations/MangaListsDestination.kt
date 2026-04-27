package dev.alvr.katana.core.ui.navigation.destinations

import kotlinx.serialization.Serializable

sealed interface MangaListsDestination : KatanaDestination {

    @Serializable data object Root : MangaListsDestination

    @Serializable data object Lists : MangaListsDestination
}
