package dev.alvr.katana

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dev.alvr.katana.navigation.bottombar.HomeNavigationBarDestination
import dev.alvr.katana.navigation.screens.ScreensNavigator
import dev.alvr.katana.ui.base.navigation.NavigationBarDestination

@Composable
@ExperimentalAnimationApi
internal fun rememberKatanaAppState(
    navController: NavHostController = rememberAnimatedNavController(),
): KatanaAppState {
    val navigator = remember(navController) { ScreensNavigator(navController) }
    val destinationRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val navigationBarDestinations = enumValues<HomeNavigationBarDestination>()
    val bottomBarRoutes = navigationBarDestinations.map { it.route }
    val shouldShowBottomBar = destinationRoute in bottomBarRoutes

    return remember(navController, navigator, navigationBarDestinations, shouldShowBottomBar) {
        KatanaAppState(navController, navigator, navigationBarDestinations, shouldShowBottomBar)
    }
}

@Stable
internal class KatanaAppState(
    val navController: NavHostController,
    val navigator: ScreensNavigator,
    val navigationBarDestinations: Array<out NavigationBarDestination>,
    val isBottomBarVisible: Boolean
) {
    private val NavGraph.startDestination: NavDestination?
        get() = findNode(startDestinationId)

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun bottomBarNavigation(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true

                findStartDestination(navController.graph)?.id?.let { id ->
                    popUpTo(id) {
                        saveState = true
                    }
                }
            }
        }
    }

    private tailrec fun findStartDestination(graph: NavDestination?): NavDestination? =
        if (graph is NavGraph) {
            findStartDestination(graph.startDestination)
        } else {
            graph
        }
}
