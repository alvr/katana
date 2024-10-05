package dev.alvr.katana.core.ui.screens

import kotlinx.serialization.Serializable

sealed interface RootScreen : KatanaScreen {
    @Serializable
    data object Auth : RootScreen

    @Serializable
    data object Home : RootScreen
}
