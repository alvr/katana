package dev.alvr.katana.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import dev.alvr.katana.navigation.homeEnterTransition
import dev.alvr.katana.navigation.homeExitTransition
import dev.alvr.katana.navigation.screens.Screen
import dev.alvr.katana.ui.account.Account
import dev.alvr.katana.ui.explore.Explore
import dev.alvr.katana.ui.lists.Lists
import dev.alvr.katana.ui.social.Social

@ExperimentalAnimationApi
internal fun NavGraphBuilder.home() {
    navigation(
        route = Screen.Home.route,
        startDestination = Screen.Home.Lists.route,
    ) {
        lists()
        explore()
        social()
        account()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.lists() {
    composable(
        route = Screen.Home.Lists.route,
        enterTransition = homeEnterTransition,
        exitTransition = homeExitTransition,
    ) {
        Lists()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.explore() {
    composable(
        route = Screen.Home.Explore.route,
        enterTransition = homeEnterTransition,
        exitTransition = homeExitTransition,
    ) {
        Explore()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.social() {
    composable(
        route = Screen.Home.Social.route,
        enterTransition = homeEnterTransition,
        exitTransition = homeExitTransition,
    ) {
        Social()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.account() {
    composable(
        route = Screen.Home.Account.route,
        enterTransition = homeEnterTransition,
        exitTransition = homeExitTransition,
    ) {
        Account()
    }
}
