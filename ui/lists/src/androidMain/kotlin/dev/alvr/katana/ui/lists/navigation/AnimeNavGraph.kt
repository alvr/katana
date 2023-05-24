package dev.alvr.katana.ui.lists.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.lists.view.destinations.AnimeScreenDestination
import dev.alvr.katana.ui.lists.view.destinations.ChangeListSheetDestination

object AnimeNavGraph : NavGraphSpec {
    override val route = "anime_lists"
    override val startRoute = AnimeScreenDestination routedIn this
    override val destinationsByRoute = listOf(
        AnimeScreenDestination,
        ChangeListSheetDestination,
    ).routedIn(this).associateBy { it.route }
}
