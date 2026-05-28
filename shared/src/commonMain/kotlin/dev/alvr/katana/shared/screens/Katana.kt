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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.KatanaSnackbarHost
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBar
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarType
import dev.alvr.katana.core.ui.components.snackbar.LocalSnackbarController
import dev.alvr.katana.core.ui.navigation.KatanaNavigationBarItem.Companion.hasRoute
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.theme.noInsets
import dev.alvr.katana.core.ui.utils.rememberSnackbarHostState
import dev.alvr.katana.core.ui.viewmodel.collectUiStateWithLifecycle
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
import kotlinx.collections.immutable.ImmutableList

/**
 * Hosts the main application UI: a scaffold with bottom navigation, navigation rail, a NavHost for screen routing,
 * a snackbar host, and a loading overlay driven by the view model state.
 *
 * The composable collects UI state from the provided view model to determine navigation items and loading visibility,
 * and wires navigation actions to the provided navigator.
 *
 * @param modifier Modifier applied to the root container.
 */
@Composable
internal fun Katana(
    modifier: Modifier = Modifier,
    navigator: RootNavigator = rememberKatanaNavigator(),
    viewModel: KatanaViewModel = metroViewModel(),
) {
    val snackbarController = LocalSnackbarController.current
    val uiState by viewModel.collectUiStateWithLifecycle()

    val items = uiState.navigationBarItems

    val snackbarHostState = rememberSnackbarHostState()
    with(snackbarHostState) { snackbarController.SnackbarMessageHandler() }

    val onItemClicked =
        remember(navigator) { { item: MainNavigationBarItem -> navigator.onNavigationBarItemClicked(item) } }

    Box(modifier = modifier) {
        KatanaScaffold(
            contentWindowInsets = WindowInsets.noInsets,
            bottomBar = {
                KatanaNavigationBarContent(
                    type = KatanaNavigationBarType.Bottom,
                    items = items,
                    navigator = navigator,
                    onItemClick = onItemClicked,
                )
            },
            snackbarHost = { KatanaSnackbarHost(hostState = snackbarHostState) },
        ) { paddingValues ->
            Row(modifier = Modifier.fillMaxSize().statusBarsPadding().displayCutoutPadding().padding(paddingValues)) {
                KatanaNavigationBarContent(
                    type = KatanaNavigationBarType.Rail,
                    items = items,
                    navigator = navigator,
                    onItemClick = onItemClicked,
                )

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

@Composable
private fun KatanaNavigationBarContent(
    type: KatanaNavigationBarType,
    items: ImmutableList<MainNavigationBarItem>,
    navigator: RootNavigator,
    onItemClick: (MainNavigationBarItem) -> Unit,
) {
    val currentNav by navigator.navController.currentBackStackEntryAsState()

    KatanaNavigationBar(
        items = items,
        isSelected = { item -> currentNav.hasRoute(item) },
        onClick = onItemClick,
        type = type,
    )
}
