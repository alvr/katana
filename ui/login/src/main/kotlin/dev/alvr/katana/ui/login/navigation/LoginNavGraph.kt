package dev.alvr.katana.ui.login.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.login.view.destinations.LoginDestination

object LoginNavGraph : NavGraphSpec {
    override val route = "login"
    override val startRoute = LoginDestination routedIn this
    override val destinationsByRoute = listOf(LoginDestination)
        .routedIn(this)
        .associateBy { it.route }
}
