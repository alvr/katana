package dev.alvr.katana.ui.explore.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.explore.view.destinations.ExploreDestination

object ExploreNavGraph : NavGraphSpec {
    override val route = "explore"
    override val startRoute = ExploreDestination routedIn this
    override val destinationsByRoute = listOf(ExploreDestination)
        .routedIn(this)
        .associateBy { it.route }
}
