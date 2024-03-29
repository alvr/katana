package dev.alvr.katana.shared.navigation

import co.touchlab.kermit.Logger
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.features.account.ui.navigation.AccountNavigator
import dev.alvr.katana.features.lists.ui.entities.UserList
import dev.alvr.katana.features.lists.ui.navigation.ListsNavigator
import dev.alvr.katana.features.lists.ui.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.features.login.ui.navigation.LoginNavigator
import dev.alvr.katana.features.login.ui.view.destinations.LoginScreenDestination

internal class Navigator(
    private val navGraph: NavGraphSpec,
    private val navigator: DestinationsNavigator,
) : AccountNavigator,
    LoginNavigator,
    ListsNavigator {

    //region [BaseNavigator]
    override fun goBack() {
        navigator.navigateUp()
    }
    //endregion [BaseNavigator]

    //region [AccountNavigator]
    override fun logout() {
        navigator.navigate(NavGraphs.root) {
            popUpTo(LoginScreenDestination) {
                inclusive = true
            }
        }
    }
    //endregion [AccountNavigator]

    //region [LoginNavigator]
    override fun toHome() {
        navigator.navigate(NavGraphs.home, onlyIfResumed = true) {
            popUpTo(LoginScreenDestination) {
                inclusive = true
            }
        }
    }
    //endregion [LoginNavigator]

    //region [ListsNavigator]
    override fun editEntry(id: Int) {
        Logger.d { "Open bottom sheet to edit entry $id" }
    }

    override fun entryDetails(id: Int) {
        Logger.d { "Navigate to media details of entry $id" }
    }

    override fun listSelector(lists: Array<UserList>, selectedList: String) {
        navigator.navigate(ChangeListSheetDestination(lists, selectedList) within navGraph)
    }
    //endregion [ListsNavigator]
}
