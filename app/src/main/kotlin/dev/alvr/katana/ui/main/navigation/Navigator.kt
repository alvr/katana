package dev.alvr.katana.ui.main.navigation

import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.alvr.katana.ui.lists.entities.UserList
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.ui.lists.view.destinations.EditEntrySheetDestination
import dev.alvr.katana.ui.login.navigation.LoginNavigator
import dev.alvr.katana.ui.login.view.destinations.LoginDestination
import io.github.aakira.napier.Napier

internal class Navigator(
    private val navGraph: NavGraphSpec,
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
        navigator.navigate(NavGraphs.home) {
            popUpTo(LoginDestination) {
                inclusive = true
            }
        }
    }
    //endregion [LoginNavigator]

    //region [ListsNavigator]
    override fun listsEditEntry(id: Int) {
        navigator.navigate(EditEntrySheetDestination within navGraph)
    }

    override fun listsEntryDetails(id: Int) {
        Napier.d { "Navigate to media details of entry $id" }
    }

    override fun listSelector(lists: Array<UserList>, selectedList: String) {
        navigator.navigate(ChangeListSheetDestination(lists, selectedList) within navGraph)
    }
    //endregion [ListsNavigator]
}
