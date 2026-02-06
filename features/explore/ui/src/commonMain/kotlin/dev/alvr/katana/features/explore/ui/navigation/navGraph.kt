package dev.alvr.katana.features.explore.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.alvr.katana.features.explore.ui.screens.ExploreScreen

fun NavGraphBuilder.explore(exploreNavigator: ExploreNavigator) {
    navigation<ExploreDestination.Root>(startDestination = ExploreDestination.Explore) {
        composable<ExploreDestination.Explore> { ExploreScreen(exploreNavigator) }
    }
}
