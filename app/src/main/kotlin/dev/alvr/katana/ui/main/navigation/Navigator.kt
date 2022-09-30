package dev.alvr.katana.ui.main.navigation

import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.alvr.katana.ui.lists.navigation.ListsNavGraph
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.destinations.ListSelectorDestination
import dev.alvr.katana.ui.login.navigation.LoginNavigator
import dev.alvr.katana.ui.login.view.destinations.LoginDestination
import io.github.aakira.napier.Napier

internal class Navigator(
    private val navigator: DestinationsNavigator,
) : LoginNavigator,
    ListsNavigator {

    //region [BaseNavigator]
    override fun goBack() {
        navigator.navigateUp()
    }
    //endregion [BaseNavigator]

    //region [LoginNavigator]
    override fun toHome() {
        navigator.navigate(NavGraphs.home, onlyIfResumed = true) {
            popUpTo(LoginDestination) {
                inclusive = true
            }
        }
    }
    //endregion [LoginNavigator]

    //region [ListsNavigator]
    override fun openEditEntry(id: Int) {
        Napier.d { "Open bottom sheet to edit entry $id" }
    }

    override fun toMediaDetails(id: Int) {
        Napier.d { "Navigate to media details of entry $id" }
    }

    override fun openListSelector(lists: Array<String>) {
        navigator.navigate(ListSelectorDestination(lists) within ListsNavGraph)
    }
    //endregion [ListsNavigator]
}