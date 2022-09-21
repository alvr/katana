package dev.alvr.katana.ui.lists.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.lists.view.destinations.AnimeScreenDestination
import dev.alvr.katana.ui.lists.view.destinations.MangaScreenDestination
import dev.alvr.katana.ui.lists.view.listsDestinations

object AnimeNavGraph : NavGraphSpec {
    override val route = "anime_lists"
    override val startRoute = AnimeScreenDestination routedIn this
    override val destinationsByRoute = (listsDestinations - MangaScreenDestination)
        .routedIn(this)
        .associateBy { it.route }
}
