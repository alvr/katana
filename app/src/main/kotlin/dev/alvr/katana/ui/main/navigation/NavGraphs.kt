package dev.alvr.katana.ui.main.navigation

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.account.navigation.AccountNavGraph
import dev.alvr.katana.ui.explore.navigation.ExploreNavGraph
import dev.alvr.katana.ui.lists.navigation.AnimeNavGraph
import dev.alvr.katana.ui.lists.navigation.MangaNavGraph
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import dev.alvr.katana.ui.social.navigation.SocialNavGraph

internal object NavGraphs {
    val home = object : NavGraphSpec {
        override val route = "home"
        override val startRoute = AnimeNavGraph
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(
            AnimeNavGraph,
            MangaNavGraph,
            SocialNavGraph,
            ExploreNavGraph,
            AccountNavGraph,
        )
    }

    val root = object : NavGraphSpec {
        override val route = "root"
        override val startRoute = LoginNavGraph
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(LoginNavGraph, home)
    }
}
