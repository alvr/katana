package dev.alvr.katana.features.social.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.features.social.ui.screens.SocialScreen

fun NavGraphBuilder.social(socialNavigator: SocialNavigator) {
    composable<HomeDestination.Social> {
        SocialScreen(socialNavigator)
    }
}
