package dev.alvr.katana.ui.social.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.social.view.destinations.SocialDestination

object SocialNavGraph : NavGraphSpec {
    override val route = "social"
    override val startRoute = SocialDestination routedIn this
    override val destinationsByRoute = listOf(SocialDestination)
        .routedIn(this)
        .associateBy { it.route }
}
