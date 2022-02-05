package dev.alvr.katana.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dev.alvr.katana.ui.login.LOGIN_DEEP_LINK
import dev.alvr.katana.ui.login.Login

@Composable
@ExperimentalAnimationApi
fun KatanaNavigator() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = { enterTransition() },
        exitTransition = { exitTransition() },
        popEnterTransition = { popEnterTransition() },
        popExitTransition = { popExitTransition() }
    ) {
        composable(
            route = Screen.Login.route,
            deepLinks = listOf(navDeepLink { uriPattern = LOGIN_DEEP_LINK })
        ) {
            Login {
                navController.navigate("home") {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.composable(
    nav: Screen,
    content: @Composable NavBackStackEntry.() -> Unit
) {
    composable(
        route = nav.route,
        arguments = nav.arguments,
    ) {
        content(it)
    }
}

private fun NavHostController.navigate(nav: Screen) {
    navigate(nav.route)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.enterTransition() =
    fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.exitTransition() =
    fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.popEnterTransition() =
    fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.popExitTransition() =
    fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
