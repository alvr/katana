package dev.alvr.katana.ui.login.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.login.view.destinations.LoginScreenDestination

object LoginNavGraph : NavGraphSpec {
    override val route = "login"
    override val startRoute = LoginScreenDestination routedIn this
    override val destinationsByRoute = listOf(LoginScreenDestination)
        .routedIn(this)
        .associateBy { it.route }
}
