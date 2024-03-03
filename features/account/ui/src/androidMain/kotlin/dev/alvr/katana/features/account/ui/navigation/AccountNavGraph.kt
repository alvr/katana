package dev.alvr.katana.features.account.ui.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.features.account.ui.view.destinations.AccountScreenDestination

object AccountNavGraph : NavGraphSpec {
    override val route = "account"
    override val startRoute = AccountScreenDestination routedIn this
    override val destinationsByRoute = listOf(AccountScreenDestination)
        .routedIn(this)
        .associateBy { it.route }
}
