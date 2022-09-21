package dev.alvr.katana.ui.lists.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.lists.view.destinations.AnimeScreenDestination
import dev.alvr.katana.ui.lists.view.destinations.MangaScreenDestination
import dev.alvr.katana.ui.lists.view.listsDestinations

object MangaNavGraph : NavGraphSpec {
    override val route = "manga_lists"
    override val startRoute = MangaScreenDestination routedIn this
    override val destinationsByRoute = (listsDestinations - AnimeScreenDestination)
        .routedIn(this)
        .associateBy { it.route }
}
