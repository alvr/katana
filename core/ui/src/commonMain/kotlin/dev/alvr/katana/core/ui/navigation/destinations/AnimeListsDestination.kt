package dev.alvr.katana.core.ui.navigation.destinations

import kotlinx.serialization.Serializable

sealed interface AnimeListsDestination : KatanaDestination {

    @Serializable data object Root : AnimeListsDestination

    @Serializable data object Lists : AnimeListsDestination
}
