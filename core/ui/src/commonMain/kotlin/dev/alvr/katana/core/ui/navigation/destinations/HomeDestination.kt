package dev.alvr.katana.core.ui.navigation.destinations

import kotlinx.serialization.Serializable

sealed interface HomeDestination : KatanaDestination {

    @Serializable data object Root : HomeDestination

    @Serializable data class Home(val token: String? = null) : HomeDestination
}
