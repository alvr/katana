package dev.alvr.katana.shared.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.login.ui.view.destinations.LoginScreenDestination
import dev.alvr.katana.shared.components.SessionExpiredDialog
import dev.alvr.katana.shared.viewmodel.MainViewModel
import io.sentry.compose.withSentryObservableEffect

@Composable
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class,
    ExperimentalLayoutApi::class,
)
internal fun KatanaDestinations(
    useNavRail: Boolean,
    vm: MainViewModel,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController().withSentryObservableEffect().apply {
        navigatorProvider += bottomSheetNavigator
    }

    val state by vm.collectAsState()

    SessionExpiredDialog(visible = !state.isSessionActive) {
        vm.clearSession()
        navController.logout()
    }

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = MaterialTheme.shapes.large,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = contentColorFor(MaterialTheme.colorScheme.surface),
        scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f),
    ) {
        Scaffold(
            bottomBar = {
                if (!useNavRail) {
                    NavigationBar(
                        modifier = Modifier.fillMaxWidth(),
                        navController = navController,
                        type = NavigationBarType.Bottom,
                    )
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .systemBarsPadding()
                    .displayCutoutPadding(),
            ) {
                if (useNavRail) {
                    NavigationBar(
                        modifier = Modifier.fillMaxHeight(),
                        navController = navController,
                        type = NavigationBarType.Rail,
                    )
                }

                DestinationsNavHost(
                    engine = rememberAnimatedNavHostEngine(
                        rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING,
                    ),
                    navController = navController,
                    navGraph = NavGraphs.root,
                    startRoute = state.initialNavGraph,
                    dependenciesContainerBuilder = {
                        dependency(currentNavigator())
                    },
                )
            }
        }
    }
}

@Composable
private fun DependenciesContainerBuilder<*>.currentNavigator(): Navigator = remember {
    Navigator(
        navGraph = navBackStackEntry.destination.navGraph(),
        navigator = destinationsNavigator,
    )
}

private fun NavController.logout() {
    navigate(LoginScreenDestination(null)) {
        popUpTo(NavGraphs.home) {
            inclusive = true
        }
    }
}

internal fun NavDestination.navGraph(
    graphs: Iterable<NavGraphSpec> = allNavGraphs
): NavGraphSpec {
    hierarchy.forEach { destination ->
        graphs.forEach { graph ->
            if (destination.route == graph.route) {
                return graph
            }
        }
    }

    error("Unknown navGraph for destination $route")
}

private fun NavGraphSpec.children() = buildSet {
    addAll(nestedNavGraphs)
    nestedNavGraphs.nested()
}

context(MutableSet<NavGraphSpec>)
private fun List<NavGraphSpec>.nested() {
    forEach { graph ->
        addAll(graph.nestedNavGraphs)
        graph.nestedNavGraphs.nested()
    }
}

private val allNavGraphs by lazy { NavGraphs.root.children() }
