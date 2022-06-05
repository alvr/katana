package dev.alvr.katana.navigation.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.utils.destination
import dev.alvr.katana.navigation.NavGraphs
import dev.alvr.katana.ui.login.navigation.LoginNavGraph

@Composable
internal fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentNavGraph = currentBackStackEntry?.destination?.navGraph()

    val isVisible = currentNavGraph !is LoginNavGraph

    @Suppress("UseIfInsteadOfWhen") // Remove when adding another NavGraph
    val bottomBarDestination: Array<out BottomBarDestinations> = when (currentNavGraph) {
        NavGraphs.home -> enumValues<HomeBottomBarDestinations>()
        else -> emptyArray()
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            val currentDestination = currentBackStackEntry?.destination()

            BottomNavigation {
                bottomBarDestination.forEach { destination ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(id = destination.label),
                            )
                        },
                        label = { Text(text = stringResource(id = destination.label)) },
                        alwaysShowLabel = true,
                        selected = currentDestination?.route in destination.direction.destinationsByRoute.keys,
                        onClick = {
                            navController.navigate(destination.direction) {
                                launchSingleTop = true
                                restoreState = true

                                findStartDestination(navController.graph)?.id?.let { id ->
                                    popUpTo(id) {
                                        saveState = true
                                    }
                                }
                            }
                        },
                    )
                }
            }
        },
    )
}

private fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }

    error("Unknown nav graph for destination $route")
}

private tailrec fun findStartDestination(graph: NavDestination?): NavDestination? =
    if (graph is NavGraph) {
        findStartDestination(graph.startDestination)
    } else {
        graph
    }

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)
