package dev.alvr.katana.ui.account.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.account.view.destinations.AccountDestination

object AccountNavGraph : NavGraphSpec {
    override val route = "account"
    override val startRoute = AccountDestination routedIn this
    override val destinationsByRoute = listOf(AccountDestination)
        .routedIn(this)
        .associateBy { it.route }
}
