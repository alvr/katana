package dev.alvr.katana.features.home.ui.screens

import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.alvr.katana.core.ui.components.navigation.katanaNavigationBar
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.core.ui.destinations.RootDestination
import dev.alvr.katana.core.ui.utils.doNavigation
import dev.alvr.katana.core.ui.viewmodel.collectEffect
import dev.alvr.katana.features.account.ui.navigation.account
import dev.alvr.katana.features.explore.ui.navigation.explore
import dev.alvr.katana.features.home.ui.navigation.HomeNavigationBarItem.Companion.hasRoute
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.navigation.homeNavigationBarItems
import dev.alvr.katana.features.home.ui.viewmodel.HomeEffect
import dev.alvr.katana.features.home.ui.viewmodel.HomeViewModel
import dev.alvr.katana.features.lists.ui.navigation.lists
import dev.alvr.katana.features.social.ui.navigation.social
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.home(homeNavigator: HomeNavigator) {
    composable<RootDestination.Home> {
        HomeScreen(homeNavigator)
    }
}

@Composable
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

    NavigationSuiteScaffold(
        modifier = Modifier
            .displayCutoutPadding()
            .statusBarsPadding(),
        navigationSuiteItems = {
            katanaNavigationBar(
                modifier = Modifier.navigationBarsPadding(),
                items = homeNavigationBarItems,
                selected = { item -> currentNav.hasRoute(item) },
                onClick = { item -> homeNavigator.onHomeNavigationBarItemClicked(item) },
            )
        },
    ) {
        NavHost(
            navController = homeNavigator.homeNavController,
            startDestination = HomeDestination.AnimeLists,
        ) {
            lists(listsNavigator = homeNavigator)
            explore(exploreNavigator = homeNavigator)
            social(socialNavigator = homeNavigator)
            account(accountNavigator = homeNavigator)

            expiredSessionDialog(homeNavigator = homeNavigator)
        }
    }
}
