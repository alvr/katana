package dev.alvr.katana.features.lists.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaDestination
import kotlinx.serialization.Serializable

sealed interface AnimeListsDestination : KatanaDestination {

    @Serializable data object Root : AnimeListsDestination

    @Serializable data object Lists : AnimeListsDestination
}
