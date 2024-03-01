package dev.alvr.katana.features.social.ui

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.features.social.ui.view.destinations.SocialScreenDestination

object SocialNavGraph : NavGraphSpec {
    override val route = "social"
    override val startRoute = SocialScreenDestination routedIn this
    override val destinationsByRoute = listOf(SocialScreenDestination)
        .routedIn(this)
        .associateBy { it.route }
}
