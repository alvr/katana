package dev.alvr.katana.features.explore.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaDestination
import kotlinx.serialization.Serializable

sealed interface ExploreDestination : KatanaDestination {

    @Serializable data object Root : ExploreDestination

    @Serializable data object Explore : ExploreDestination
}
