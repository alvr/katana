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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.alvr.katana.core.ui.components.KatanaScaffold
import dev.alvr.katana.core.ui.components.KatanaSnackbarHost
import dev.alvr.katana.core.ui.components.snackbar.LocalSnackbarController
import dev.alvr.katana.core.ui.navigation.LocalNavigator
import dev.alvr.katana.core.ui.navigation.rememberBottomSheetSceneStrategy
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.utils.rememberSnackbarHostState
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.shared.viewmodel.KatanaViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun Katana(modifier: Modifier = Modifier, viewModel: KatanaViewModel = metroViewModel()) {
    val navigator = LocalNavigator.current
    val snackbarController = LocalSnackbarController.current
    val uiState by viewModel.collectAsState()

    val items = uiState.navigationBarItems

    val snackbarHostState = rememberSnackbarHostState()
    with(snackbarHostState) { snackbarController.SnackbarMessageHandler() }

    Box(modifier = modifier) {
        NavigationSuiteScaffold(
            navigationItemVerticalArrangement = Arrangement.Center,
            navigationItems = {
                items.fastForEach { item ->
                    NavigationSuiteItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label.value) },
                        label = { Text(text = item.label.value) },
                        selected = false,
                        onClick = {},
                    )
                }
            },
        ) {
            KatanaScaffold(snackbarHost = { KatanaSnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
                NavDisplay(
                    backStack = navigator.backStack,
                    onBack = navigator::goBack,
                    modifier = Modifier.fillMaxSize().statusBarsPadding().displayCutoutPadding().padding(paddingValues),
                    sceneStrategies = listOf(rememberBottomSheetSceneStrategy(), rememberListDetailSceneStrategy()),
                    entryDecorators =
                        listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                    entryProvider = navigator.entryProvider,
                )
            }
        }

        AnimatedVisibility(uiState.loading) {
            Box(modifier = Modifier.fillMaxSize().background(KatanaTheme.colorScheme.background))
        }
    }
}
