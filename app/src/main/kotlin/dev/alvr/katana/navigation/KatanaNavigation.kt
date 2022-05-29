package dev.alvr.katana.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope

@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
internal fun KatanaNavigator() {
    val navController = rememberAnimatedNavController()

    DestinationsNavHost(
        engine = rememberAnimatedNavHostEngine(
            rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING,
        ),
        navController = navController,
        navGraph = NavGraph.root,
        dependenciesContainerBuilder = {
            dependency(currentNavigator())
        },
    )
}

@Composable
private fun DestinationScope<*>.currentNavigator(): Navigator = remember {
    Navigator(navController = navController)
}
