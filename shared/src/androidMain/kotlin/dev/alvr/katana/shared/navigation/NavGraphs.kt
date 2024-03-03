package dev.alvr.katana.shared.navigation

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.features.account.ui.navigation.AccountNavGraph
import dev.alvr.katana.features.explore.ui.navigation.ExploreNavGraph
import dev.alvr.katana.features.lists.ui.navigation.AnimeNavGraph
import dev.alvr.katana.features.lists.ui.navigation.MangaNavGraph
import dev.alvr.katana.features.login.ui.navigation.LoginNavGraph
import dev.alvr.katana.features.social.ui.navigation.SocialNavGraph

internal object NavGraphs {
    internal val home = object : NavGraphSpec {
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

    internal val root = object : NavGraphSpec {
        override val route = "root"
        override val startRoute = LoginNavGraph
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(LoginNavGraph, home)
    }
}
