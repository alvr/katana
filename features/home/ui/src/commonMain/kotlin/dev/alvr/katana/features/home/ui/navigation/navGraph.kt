package dev.alvr.katana.features.home.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.alvr.katana.core.ui.utils.navDeepLink
import dev.alvr.katana.features.home.ui.LOGIN_DEEP_LINK
import dev.alvr.katana.features.home.ui.LOGIN_DEEP_LINK_TOKEN
import dev.alvr.katana.features.home.ui.screens.HomeScreen
import dev.alvr.katana.features.home.ui.viewmodel.HomeViewModel
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

/**
 * Registers the "home" navigation graph and its Home destination.
 *
 * The destination is configured with a deep link (LOGIN_DEEP_LINK). When navigated to, a
 * HomeViewModel is created via assistedMetroViewModel with the `token` argument read and
 * removed from the destination's SavedStateHandle using the key LOGIN_DEEP_LINK_TOKEN, and
 * HomeScreen is rendered with the created view model.
 *
 * @param homeNavigator Navigator used by the HomeScreen to perform navigation actions.
 */
fun NavGraphBuilder.home(homeNavigator: HomeNavigator) {
    navigation<HomeDestination.Root>(startDestination = HomeDestination.Home()) {
        composable<HomeDestination.Home>(deepLinks = listOf(navDeepLink { setUriPattern(LOGIN_DEEP_LINK) })) {
            homeBackStackEntry ->
            val viewModel =
                assistedMetroViewModel<HomeViewModel, HomeViewModel.Factory> {
                    create(token = homeBackStackEntry.savedStateHandle.remove<String>(LOGIN_DEEP_LINK_TOKEN))
                }

            HomeScreen(homeNavigator = homeNavigator, viewModel = viewModel)
        }
    }
}
