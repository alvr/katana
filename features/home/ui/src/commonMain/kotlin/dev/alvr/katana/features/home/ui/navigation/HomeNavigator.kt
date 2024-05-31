package dev.alvr.katana.features.home.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import dev.alvr.katana.core.ui.navigation.rememberKatanaNavigator
import dev.alvr.katana.core.ui.screens.KatanaScreen
import dev.alvr.katana.features.account.ui.navigation.AccountNavigator
import dev.alvr.katana.features.explore.ui.navigation.ExploreNavigator
import dev.alvr.katana.features.lists.ui.entities.UserList
import dev.alvr.katana.features.lists.ui.navigation.ListsNavigator
import dev.alvr.katana.features.social.ui.navigation.SocialNavigator

interface HomeNavigator : AccountNavigator, ExploreNavigator, ListsNavigator, SocialNavigator {
    val homeNavController: NavHostController

    override fun navigateToLogin()

    fun onSessionExpired()

    fun onNavigationBarItemClicked(screen: KatanaScreen)
}

private class DefaultKatanaHomeNavigator(
    override val homeNavController: NavHostController,
) : HomeNavigator {

    override fun navigateBack() {
        homeNavController.navigateUp()
    }

    override fun <T> popBackStackWithResult(result: T) {
        homeNavController.previousBackStackEntry?.savedStateHandle?.set(NAV_RESULT, result)
        homeNavController.popBackStack()
    }

    override fun <T> getNavigationResult(onResult: (T) -> Unit) {
        homeNavController.currentBackStackEntry?.savedStateHandle?.run {
            get<T>(NAV_RESULT)?.let(onResult)
            remove<T>(NAV_RESULT)
        }
    }

    override fun onSessionExpired() {
        homeNavController.navigate(KatanaScreen.ExpiredSessionDialog.name)
    }

    override fun onNavigationBarItemClicked(screen: KatanaScreen) {
        homeNavController.navigate(screen.name) {
            popUpTo(homeNavController.graph.startDestinationRoute.orEmpty()) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    override fun navigateToLogin() {
        Logger.i { "Implementation overridden in KatanaRootNavigator" }
    }

    override fun navigateToEntryDetails(id: Int) {
        Logger.d { "Entry details $id" }
    }

    override fun showEditEntry(id: Int) {
        Logger.d { "Edit entry $id" }
    }

    override fun showListSelector(lists: Array<UserList>, selectedList: String) {
        Logger.d { "Showing list selector bottom sheet" }
    }

    private companion object {
        const val NAV_RESULT = "NavResult"
    }
}

@Composable
fun rememberKatanaHomeNavigator(): HomeNavigator = rememberKatanaNavigator { navController ->
    DefaultKatanaHomeNavigator(homeNavController = navController)
}
