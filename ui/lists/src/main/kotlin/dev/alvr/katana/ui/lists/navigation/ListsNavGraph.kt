package dev.alvr.katana.ui.lists.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.lists.view.destinations.ListsScreenDestination
import dev.alvr.katana.ui.lists.view.listsDestinations

object ListsNavGraph : NavGraphSpec {
    override val route = "lists"
    override val startRoute = ListsScreenDestination routedIn this
    override val destinationsByRoute = listsDestinations
        .routedIn(this)
        .associateBy { it.route }
}
