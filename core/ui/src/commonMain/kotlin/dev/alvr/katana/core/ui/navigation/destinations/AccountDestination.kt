package dev.alvr.katana.core.ui.navigation.destinations

import kotlinx.serialization.Serializable

sealed interface AccountDestination : KatanaDestination {

    @Serializable data object Root : AccountDestination

    @Serializable data object Account : AccountDestination
}
