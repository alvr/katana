package dev.alvr.katana.ui.account.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.account.view.destinations.AccountScreenDestination

object AccountNavGraph : NavGraphSpec {
    override val route = "account"
    override val startRoute = AccountScreenDestination routedIn this
    override val destinationsByRoute = listOf(AccountScreenDestination)
        .routedIn(this)
        .associateBy { it.route }
}
