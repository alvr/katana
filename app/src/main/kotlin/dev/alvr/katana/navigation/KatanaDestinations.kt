package dev.alvr.katana.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.scope.DestinationScope
import dev.alvr.katana.navigation.bottombar.BottomNavigationBar
import dev.alvr.katana.ui.login.view.destinations.LoginDestination
import dev.alvr.katana.ui.main.MainViewModel
import dev.alvr.katana.ui.main.components.SessionExpiredDialog
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
internal fun KatanaDestinations() {
    val navController = rememberAnimatedNavController()
    val vm = hiltViewModel<MainViewModel>()

    val state by vm.collectAsState()

    SessionExpiredDialog(visible = !state.isSessionActive) {
        vm.clearSession()
        navController.logout()
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
    ) { paddingValues ->
        DestinationsNavHost(
            modifier = Modifier.padding(paddingValues),
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

@Composable
private fun DestinationScope<*>.currentNavigator(): Navigator = remember {
    Navigator(navigator = destinationsNavigator)
}

private fun NavController.logout() {
    navigate(LoginDestination) {
        popUpTo(NavGraphs.home) {
            inclusive = true
        }
    }
}
