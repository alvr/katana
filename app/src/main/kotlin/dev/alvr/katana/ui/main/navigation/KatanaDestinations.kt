package dev.alvr.katana.ui.main.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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
import dev.alvr.katana.ui.login.view.destinations.LoginDestination
import dev.alvr.katana.ui.main.components.SessionExpiredDialog
import dev.alvr.katana.ui.main.viewmodel.MainViewModel
import io.sentry.compose.withSentryObservableEffect
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
internal fun KatanaDestinations(
    useNavRail: Boolean,
    vm: MainViewModel,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController().withSentryObservableEffect().also { nav ->
        nav.navigatorProvider += bottomSheetNavigator
    }

    val state by vm.collectAsState()

    SessionExpiredDialog(visible = !state.isSessionActive) {
        vm.clearSession()
        navController.logout()
    }

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(16.dp),
    ) {
        Scaffold(
            bottomBar = {
                if (!useNavRail) {
                    NavigationBar(
                        modifier = Modifier.fillMaxWidth(),
                        navController = navController,
                        type = NavigationBarType.Bottom,
                    )
                } else {
                    Spacer(
                        Modifier
                            .windowInsetsBottomHeight(WindowInsets.navigationBars)
                            .fillMaxWidth(),
                    )
                }
            },
        ) { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
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
    Navigator(navigator = destinationsNavigator)
}

private fun NavController.logout() {
    navigate(LoginDestination) {
        popUpTo(NavGraphs.home) {
            inclusive = true
        }
    }
}
