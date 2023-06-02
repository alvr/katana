package dev.alvr.katana.ui.explore.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.explore.view.destinations.ExploreScreenDestination

object ExploreNavGraph : NavGraphSpec {
    override val route = "explore"
    override val startRoute = ExploreScreenDestination routedIn this
    override val destinationsByRoute = listOf(ExploreScreenDestination)
        .routedIn(this)
        .associateBy { it.route }
}
