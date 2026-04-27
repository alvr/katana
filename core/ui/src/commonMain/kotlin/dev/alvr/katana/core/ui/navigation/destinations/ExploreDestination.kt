package dev.alvr.katana.core.ui.navigation.destinations

import kotlinx.serialization.Serializable

sealed interface ExploreDestination : KatanaDestination {

    @Serializable data object Root : ExploreDestination

    @Serializable data object Explore : ExploreDestination
}
