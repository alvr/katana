package dev.alvr.katana.features.explore.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.features.explore.ui.screens.ExploreScreen

fun NavGraphBuilder.explore(exploreNavigator: ExploreNavigator) {
    composable<HomeDestination.Explore> {
        ExploreScreen(exploreNavigator)
    }
}
