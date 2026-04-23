package dev.alvr.katana.shared.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.KatanaSnackbarHost
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBar
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarType
import dev.alvr.katana.core.ui.components.snackbar.SnackbarController
import dev.alvr.katana.core.ui.navigation.KatanaNavigationBarItem.Companion.hasRoute
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.theme.noInsets
import dev.alvr.katana.core.ui.utils.rememberSnackbarHostState
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.account.ui.navigation.account
import dev.alvr.katana.features.explore.ui.navigation.explore
import dev.alvr.katana.features.home.ui.navigation.HomeDestination
import dev.alvr.katana.features.home.ui.navigation.home
import dev.alvr.katana.features.lists.ui.navigation.lists
import dev.alvr.katana.shared.navigation.MainNavigationBarItem
import dev.alvr.katana.shared.navigation.RootNavigator
import dev.alvr.katana.shared.navigation.rememberKatanaNavigator
import dev.alvr.katana.shared.viewmodel.KatanaViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
internal fun Katana(
    snackbarController: SnackbarController,
    modifier: Modifier = Modifier,
    navigator: RootNavigator = rememberKatanaNavigator(),
    viewModel: KatanaViewModel = metroViewModel(),
) {
    val currentNav by navigator.navController.currentBackStackEntryAsState()
    val uiState by viewModel.collectAsState()

    val items = uiState.navigationBarItems
    val currentNavEntry by rememberUpdatedState(currentNav)

    val snackbarHostState = rememberSnackbarHostState()
    with(snackbarHostState) { snackbarController.SnackbarMessageHandler() }

    val onItemClicked =
        remember(navigator) { { item: MainNavigationBarItem -> navigator.onNavigationBarItemClicked(item) } }

    val isSelected = remember(currentNavEntry) { { item: MainNavigationBarItem -> currentNavEntry.hasRoute(item) } }

    val navigationBar =
        @Composable { type: KatanaNavigationBarType ->
            KatanaNavigationBar(items = items, isSelected = isSelected, onClick = onItemClicked, type = type)
        }

    Box(modifier = modifier) {
        KatanaScaffold(
            contentWindowInsets = WindowInsets.noInsets,
            bottomBar = { navigationBar(KatanaNavigationBarType.Bottom) },
            snackbarHost = { KatanaSnackbarHost(hostState = snackbarHostState) },
        ) { paddingValues ->
            Row(modifier = Modifier.fillMaxSize().statusBarsPadding().displayCutoutPadding().padding(paddingValues)) {
                navigationBar(KatanaNavigationBarType.Rail)

                NavHost(
                    modifier = Modifier,
                    navController = navigator.navController,
                    startDestination = HomeDestination.Root,
                ) {
                    home(homeNavigator = navigator)
                    lists(animeListsNavigator = navigator, mangaListsNavigator = navigator)
                    explore(exploreNavigator = navigator)
                    account(accountNavigator = navigator)
                }
            }
        }

        AnimatedVisibility(uiState.loading) {
            Box(modifier = Modifier.fillMaxSize().background(KatanaTheme.colorScheme.background))
        }
    }
}
