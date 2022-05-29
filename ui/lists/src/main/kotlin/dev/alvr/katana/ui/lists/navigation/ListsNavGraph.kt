package dev.alvr.katana.ui.lists.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.lists.view.destinations.ListsDestination

object ListsNavGraph : NavGraphSpec {
    override val route = "lists"
    override val startRoute = ListsDestination routedIn this
    override val destinationsByRoute = listOf(ListsDestination)
        .routedIn(this)
        .associateBy { it.route }
}
