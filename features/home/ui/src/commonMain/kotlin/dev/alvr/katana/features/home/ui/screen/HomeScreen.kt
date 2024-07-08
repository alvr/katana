package dev.alvr.katana.features.home.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBar
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarType
import dev.alvr.katana.core.ui.screens.HomeScreen
import dev.alvr.katana.core.ui.screens.RootScreen
import dev.alvr.katana.core.ui.utils.doNavigation
import dev.alvr.katana.core.ui.utils.noInsets
import dev.alvr.katana.core.ui.viewmodel.collectEffect
import dev.alvr.katana.features.account.ui.screen.account
import dev.alvr.katana.features.explore.ui.screen.explore
import dev.alvr.katana.features.home.ui.navigation.HomeNavigationBarItem.Companion.hasRoute
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.navigation.homeNavigationBarItems
import dev.alvr.katana.features.home.ui.viewmodel.HomeEffect
import dev.alvr.katana.features.home.ui.viewmodel.HomeViewModel
import dev.alvr.katana.features.lists.ui.screen.lists
import dev.alvr.katana.features.social.ui.screen.social
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

fun NavGraphBuilder.home(homeNavigator: HomeNavigator) {
    composable<RootScreen.Home> {
        HomeScreen(homeNavigator)
    }
}

@Composable
@OptIn(KoinExperimentalAPI::class)
private fun HomeScreen(
    homeNavigator: HomeNavigator,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val currentNav by homeNavigator.homeNavController.currentBackStackEntryAsState()

    val sessionExpired by rememberUpdatedState(doNavigation(homeNavigator::onSessionExpired))
    viewModel.collectEffect { effect ->
        when (effect) {
            HomeEffect.ExpiredToken -> sessionExpired()
        }
    }

    val navigationBar = @Composable { type: KatanaNavigationBarType ->
        KatanaNavigationBar(
            items = homeNavigationBarItems,
            isSelected = { item -> currentNav.hasRoute(item) },
            onClick = { item -> homeNavigator.onHomeNavigationBarItemClicked(item) },
            type = type,
        )
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .displayCutoutPadding(),
        contentWindowInsets = WindowInsets.noInsets,
        bottomBar = { navigationBar(KatanaNavigationBarType.Bottom) },
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            navigationBar(KatanaNavigationBarType.Rail)

            NavHost(
                navController = homeNavigator.homeNavController,
                startDestination = HomeScreen.AnimeLists,
            ) {
                lists(listsNavigator = homeNavigator)
                explore(exploreNavigator = homeNavigator)
                social(socialNavigator = homeNavigator)
                account(accountNavigator = homeNavigator)

                expiredSessionDialog(homeNavigator = homeNavigator)
            }
        }
    }
}
