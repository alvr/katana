package dev.alvr.katana.ui.main.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.compose.currentBackStackEntryAsState
import com.moriatsushi.insetsx.systemBars
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.destination
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import dev.alvr.katana.ui.main.navigation.items.HomeNavigationBarItem
import dev.alvr.katana.ui.main.navigation.items.NavigationBarItem
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

    val isItemSelected = { item: NavigationBarItem ->
        currentDestination?.route in item.direction.destinationsByRoute.keys
    }

    val onDestinationClick = { destination: NavigationBarItem ->
        navController.navigate(destination)
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
                onClick = onDestinationClick,
            )
            NavigationBarType.Rail -> RailNavigationBar(
                destinations = destinations,
                isItemSelected = isItemSelected,
                onClick = onDestinationClick,
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    destinations: ImmutableList<NavigationBarItem>,
    isItemSelected: (NavigationBarItem) -> Boolean,
    onClick: (NavigationBarItem) -> Unit,
) {
    BottomNavigation {
        destinations.forEach { destination ->
            BottomNavigationItem(
                icon = { NavigationBarIcon(destination) },
                label = { NavigationBarLabel(destination) },
                selected = isItemSelected(destination),
                onClick = { onClick(destination) },
                alwaysShowLabel = false,
            )
        }
    }
}

@Composable
private fun RailNavigationBar(
    destinations: ImmutableList<NavigationBarItem>,
    isItemSelected: (NavigationBarItem) -> Boolean,
    onClick: (NavigationBarItem) -> Unit,
) {
    NavigationRail(
        modifier = Modifier.padding(
            WindowInsets.systemBars
                .only(WindowInsetsSides.Start + WindowInsetsSides.Vertical)
                .asPaddingValues(),
        ),
    ) {
        Spacer(Modifier.weight(1f))
        destinations.forEach { destination ->
            NavigationRailItem(
                icon = { NavigationBarIcon(destination) },
                label = { NavigationBarLabel(destination) },
                selected = isItemSelected(destination),
                selectedContentColor = LocalContentColor.current,
                onClick = { onClick(destination) },
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
