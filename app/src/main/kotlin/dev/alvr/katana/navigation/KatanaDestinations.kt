package dev.alvr.katana.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope
import dev.alvr.katana.MainViewModel
import dev.alvr.katana.navigation.bottombar.BottomNavigationBar

@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
internal fun KatanaDestinations() {
    val navController = rememberAnimatedNavController()
    val viewModel = hiltViewModel<MainViewModel>()

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
            startRoute = viewModel.initialRoute,
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
