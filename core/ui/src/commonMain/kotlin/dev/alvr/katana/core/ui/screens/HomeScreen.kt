package dev.alvr.katana.core.ui.screens

import kotlinx.serialization.Serializable

sealed interface HomeScreen : KatanaScreen {
    @Serializable
    data object AnimeLists : HomeScreen
    @Serializable
    data object MangaLists : HomeScreen
    @Serializable
    data object Explore : HomeScreen
    @Serializable
    data object Social : HomeScreen
    @Serializable
    data object Account : HomeScreen

    @Serializable
    data object ExpiredSessionDialog : HomeScreen
}
