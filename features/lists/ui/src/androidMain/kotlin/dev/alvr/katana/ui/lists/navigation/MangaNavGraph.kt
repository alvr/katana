package dev.alvr.katana.ui.lists.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.lists.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.ui.lists.view.destinations.MangaScreenDestination

object MangaNavGraph : NavGraphSpec {
    override val route = "manga_lists"
    override val startRoute = MangaScreenDestination routedIn this
    override val destinationsByRoute = listOf(
        MangaScreenDestination,
        ChangeListSheetDestination,
    ).routedIn(this).associateBy { it.route }
}
