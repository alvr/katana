package dev.alvr.katana.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dev.alvr.katana.MainViewModel
import dev.alvr.katana.navigation.destinations.home
import dev.alvr.katana.navigation.destinations.login
import dev.alvr.katana.rememberKatanaAppState
import dev.alvr.katana.ui.base.components.BottomNavigationBar

@Composable
@OptIn(ExperimentalAnimationApi::class)
internal fun KatanaNavigator() {
    val appState = rememberKatanaAppState()
    val vm = viewModel<MainViewModel>()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                isVisible = appState.isBottomBarVisible,
                currentRoute = appState.currentRoute,
                destinations = appState.navigationBarDestinations,
                navigation = appState::bottomBarNavigation,
            )
        },
    ) { paddingValues ->
        AnimatedNavHost(
            modifier = Modifier.padding(paddingValues),
            navController = appState.navController,
            startDestination = vm.initialRoute,
        ) {
            login(appState.navigator)
            home()
        }
    }
}
