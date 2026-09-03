package dev.alvr.katana.shared.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import androidx.navigation3.ui.NavDisplay
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.KatanaSnackbarHost
import dev.alvr.katana.core.ui.components.snackbar.LocalSnackbarController
import dev.alvr.katana.core.ui.navigation.KatanaNavigator
import dev.alvr.katana.core.ui.navigation.LocalNavigator
import dev.alvr.katana.core.ui.navigation.destinations.TopLevelDestination
import dev.alvr.katana.core.ui.navigation.rememberBottomSheetSceneStrategy
import dev.alvr.katana.core.ui.navigation.rememberNavState
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.utils.rememberSnackbarHostState
import dev.alvr.katana.core.ui.viewmodel.collectUiStateWithLifecycle
import dev.alvr.katana.shared.viewmodel.KatanaViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlin.collections.listOf
import kotlinx.collections.immutable.persistentSetOf

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun Katana(
    navigator: KatanaNavigator.Factory,
    modifier: Modifier = Modifier,
    viewModel: KatanaViewModel = metroViewModel(),
) {
    val navigatorState =
        rememberNavState(
            startDestination = TopLevelDestination.Home(),
            primaryTopLevelDestination = TopLevelDestination.Home(),
            topLevelDestinations =
                persistentSetOf(
                    TopLevelDestination.Home(),
                    TopLevelDestination.Anime,
                    TopLevelDestination.Manga,
                    TopLevelDestination.Explore,
                    TopLevelDestination.Account,
                ),
        )
    val navigator = remember<KatanaNavigator> { navigator.create(navigatorState) }

    val snackbarController = LocalSnackbarController.current
    val uiState by viewModel.collectUiStateWithLifecycle()

    val snackbarHostState = rememberSnackbarHostState()
    with(snackbarHostState) { snackbarController.SnackbarMessageHandler() }

    Box(modifier = modifier) {
        NavigationSuiteScaffold(
            navigationItemVerticalArrangement = Arrangement.Center,
            navigationItems = {
                uiState.navigationBarItems.fastForEach { item ->
                    NavigationSuiteItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label.value) },
                        label = { Text(text = item.label.value) },
                        selected = item.screen == navigatorState.bottomBarDestination,
                        onClick = { navigator.add(item.screen) },
                    )
                }
            },
        ) {
            KatanaScaffold(snackbarHost = { KatanaSnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
                CompositionLocalProvider(LocalNavigator provides navigator) {
                    NavDisplay(
                        entries = navigator.entries,
                        onBack = navigator::goBack,
                        modifier =
                            Modifier.fillMaxSize().statusBarsPadding().displayCutoutPadding().padding(paddingValues),
                        sceneStrategies = listOf(rememberBottomSheetSceneStrategy(), rememberListDetailSceneStrategy()),
                    )
                }
            }
        }

        AnimatedVisibility(uiState.loading) {
            Box(modifier = Modifier.fillMaxSize().background(KatanaTheme.colorScheme.background))
        }
    }
}
