package dev.alvr.katana.shared.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.utils.destination
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import dev.alvr.katana.features.login.ui.navigation.LoginNavGraph
import dev.alvr.katana.shared.navigation.items.HomeNavigationBarItem
import dev.alvr.katana.shared.navigation.items.NavigationBarItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun NavigationBar(
    navController: NavController,
    type: NavigationBarType,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentNavGraph by remember(currentBackStackEntry) {
        derivedStateOf(referentialEqualityPolicy()) {
            currentBackStackEntry?.destination?.navGraph(NavGraphs.root.nestedNavGraphs)
        }
    }

    val isVisible by remember(currentNavGraph) {
        derivedStateOf { currentNavGraph != null && currentNavGraph !is LoginNavGraph }
    }

    @Suppress("UseIfInsteadOfWhen") // Remove when adding another NavGraph
    val destinations = when (currentNavGraph) {
        NavGraphs.home -> HomeNavigationBarItem.values
        else -> persistentListOf()
    }

    val currentDestination = currentBackStackEntry?.destination()

    val isItemSelected by rememberUpdatedState { item: NavigationBarItem ->
        currentDestination?.route in item.direction.destinationsByRoute.keys
    }

    val onDestinationClick by rememberUpdatedState { destination: NavigationBarItem ->
        navController.navigate(destination.direction) {
            popUpTo(NavGraphs.home) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val isRouteOnBackStack by rememberUpdatedState { destination: NavigationBarItem ->
        navController.isRouteOnBackStack(destination.direction)
    }

    val (enterAnimation, exitAnimation) = when (type) {
        NavigationBarType.Bottom -> slideInVertically { it } to slideOutVertically { it }
        NavigationBarType.Rail -> slideInHorizontally { -it } to slideOutHorizontally { -it }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = enterAnimation,
        exit = exitAnimation,
    ) {
        when (type) {
            NavigationBarType.Bottom -> BottomNavigationBar(
                destinations = destinations,
                isItemSelected = isItemSelected,
                isRouteOnBackStack = isRouteOnBackStack,
                onClick = onDestinationClick,
            )

            NavigationBarType.Rail -> RailNavigationBar(
                destinations = destinations,
                isItemSelected = isItemSelected,
                isRouteOnBackStack = isRouteOnBackStack,
                onClick = onDestinationClick,
            )
        }
    }
}

@Composable
@Suppress("LabeledExpression")
private fun BottomNavigationBar(
    destinations: ImmutableList<NavigationBarItem>,
    isItemSelected: (NavigationBarItem) -> Boolean,
    isRouteOnBackStack: (NavigationBarItem) -> Boolean,
    onClick: (NavigationBarItem) -> Unit,
) {
    NavigationBar {
        destinations.forEach { destination ->
            val isCurrentDestOnBackStack = isRouteOnBackStack(destination)

            NavigationBarItem(
                icon = { NavigationBarIcon(destination) },
                label = { NavigationBarLabel(destination) },
                selected = isItemSelected(destination),
                onClick = {
                    if (isCurrentDestOnBackStack) return@NavigationBarItem
                    onClick(destination)
                },
                alwaysShowLabel = false,
            )
        }
    }
}

@Composable
@Suppress("LabeledExpression")
private fun RailNavigationBar(
    destinations: ImmutableList<NavigationBarItem>,
    isItemSelected: (NavigationBarItem) -> Boolean,
    isRouteOnBackStack: (NavigationBarItem) -> Boolean,
    onClick: (NavigationBarItem) -> Unit,
) {
    NavigationRail {
        Spacer(Modifier.weight(1f))
        destinations.forEach { destination ->
            val isCurrentDestOnBackStack = isRouteOnBackStack(destination)

            NavigationRailItem(
                icon = { NavigationBarIcon(destination) },
                label = { NavigationBarLabel(destination) },
                selected = isItemSelected(destination),
                onClick = {
                    if (isCurrentDestOnBackStack) return@NavigationRailItem
                    onClick(destination)
                },
                alwaysShowLabel = false,
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun NavigationBarIcon(destination: NavigationBarItem) {
    Icon(
        imageVector = destination.icon,
        contentDescription = stringResource(destination.label),
    )
}

@Composable
private fun NavigationBarLabel(destination: NavigationBarItem) {
    Text(text = stringResource(destination.label))
}

private fun NavController.navigate(destination: NavigationBarItem) {
    navigate(destination.direction) {
        launchSingleTop = true
        restoreState = true

        findStartDestination(graph)?.id?.let { id ->
            popUpTo(id) {
                saveState = true
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

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

internal enum class NavigationBarType {
    Bottom,
    Rail,
}
