package dev.alvr.katana.features.lists.ui.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.features.lists.ui.view.destinations.AnimeScreenDestination
import dev.alvr.katana.features.lists.ui.view.destinations.ChangeListSheetDestination

object AnimeNavGraph : NavGraphSpec {
    override val route = "anime_lists"
    override val startRoute = AnimeScreenDestination routedIn this
    override val destinationsByRoute = listOf(
        AnimeScreenDestination,
        ChangeListSheetDestination,
    ).routedIn(this).associateBy { it.route }
}
