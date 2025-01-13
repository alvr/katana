package dev.alvr.katana.shared.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import dev.alvr.katana.core.common.KatanaBuildConfig
import dev.alvr.katana.core.ui.navigation.KatanaDestination
import dev.alvr.katana.core.ui.utils.hasRoute
import dev.alvr.katana.features.account.ui.navigation.AccountNavigator
import dev.alvr.katana.features.account.ui.navigation.katanaAccountNavigator
import dev.alvr.katana.features.explore.ui.navigation.ExploreDestination
import dev.alvr.katana.features.explore.ui.navigation.ExploreNavigator
import dev.alvr.katana.features.explore.ui.navigation.katanaExploreNavigator
import dev.alvr.katana.features.home.ui.navigation.HomeDestination
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.navigation.katanaHomeNavigator
import dev.alvr.katana.features.lists.ui.navigation.AnimeListsDestination
import dev.alvr.katana.features.lists.ui.navigation.AnimeListsNavigator
import dev.alvr.katana.features.lists.ui.navigation.MangaListsDestination
import dev.alvr.katana.features.lists.ui.navigation.MangaListsNavigator
import dev.alvr.katana.features.lists.ui.navigation.katanaAnimeListsNavigator
import dev.alvr.katana.features.lists.ui.navigation.katanaMangaListsNavigator

internal sealed interface RootNavigator :
    HomeNavigator,
    AnimeListsNavigator,
    MangaListsNavigator,
    ExploreNavigator,
    AccountNavigator {

    fun navigateToHome()

    fun onNavigationBarItemClicked(item: MainNavigationBarItem)
}

private class KatanaRootNavigator(
    override val navController: NavHostController,
    homeNavigator: HomeNavigator,
    animeListsNavigator: AnimeListsNavigator,
    mangaListsNavigator: MangaListsNavigator,
    exploreNavigator: ExploreNavigator,
    accountNavigator: AccountNavigator,
) : RootNavigator,
    HomeNavigator by homeNavigator,
    AnimeListsNavigator by animeListsNavigator,
    MangaListsNavigator by mangaListsNavigator,
    ExploreNavigator by exploreNavigator,
    AccountNavigator by accountNavigator {

    // region [RootNavigator]
    override fun navigateBack() {
        navController.navigateUp()
    }

    override fun navigateToHome() {
        onNavigationBarItemClicked(HomeDestination.Root)
    }

    override fun onNavigationBarItemClicked(item: MainNavigationBarItem) {
        onNavigationBarItemClicked(item.screen)
    }
    // endregion [RootNavigator]

    // region [HomeNavigator]
    override fun navigateToAnimeLists() {
        onNavigationBarItemClicked(AnimeListsDestination.Root)
    }

    override fun navigateToMangaLists() {
        onNavigationBarItemClicked(MangaListsDestination.Root)
    }

    override fun navigateToTrending() {
        onNavigationBarItemClicked(ExploreDestination.Root)
    }

    override fun navigateToPopular() {
        onNavigationBarItemClicked(ExploreDestination.Root)
    }

    override fun navigateToUpcoming() {
        onNavigationBarItemClicked(ExploreDestination.Root)
    }
    // endregion [HomeNavigator]

    private fun onNavigationBarItemClicked(screen: KatanaDestination) {
        val hasItemRoute = navController.currentBackStackEntry.hasRoute(screen::class)

        if (!hasItemRoute) {
            navController.navigate(screen) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}

@Composable
internal fun rememberKatanaNavigator(): RootNavigator {
    val navController = rememberNavController().loggerObserver()

    return remember {
        KatanaRootNavigator(
            navController = navController,
            homeNavigator = katanaHomeNavigator(navController),
            animeListsNavigator = katanaAnimeListsNavigator(navController),
            mangaListsNavigator = katanaMangaListsNavigator(navController),
            exploreNavigator = katanaExploreNavigator(navController),
            accountNavigator = katanaAccountNavigator(navController),
        )
    }
}

@Composable
private fun NavHostController.loggerObserver() = apply {
    if (KatanaBuildConfig.DEBUG) {
        DisposableEffect(this, LocalLifecycleOwner.current.lifecycle) {
            val listener = NavController.OnDestinationChangedListener { navController, destination, args ->
                Logger.d(LogTag) {
                    buildString {
                        append("Navigating to route ${destination.route}")

                        navController.previousBackStackEntry?.destination?.route?.let { previousRoute ->
                            append(" from $previousRoute")
                        }
                    }
                }
            }

            addOnDestinationChangedListener(listener)
            onDispose { removeOnDestinationChangedListener(listener) }
        }
    }
}

private const val LogTag = "KatanaNavigator"
