package dev.alvr.katana.core.ui.destinations

import kotlinx.serialization.Serializable

sealed interface HomeDestination : KatanaDestination {
    @Serializable
    data object AnimeLists : HomeDestination
    @Serializable
    data object MangaLists : HomeDestination
    @Serializable
    data object Explore : HomeDestination
    @Serializable
    data object Social : HomeDestination
    @Serializable
    data object Account : HomeDestination

    @Serializable
    data object ExpiredSessionDialog : HomeDestination
}
