package dev.alvr.katana.ui.main.navigation

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.account.navigation.AccountNavGraph
import dev.alvr.katana.ui.explore.navigation.ExploreNavGraph
import dev.alvr.katana.ui.lists.navigation.ListsNavGraph
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import dev.alvr.katana.ui.social.navigation.SocialNavGraph

internal object NavGraphs {
    internal val home = object : NavGraphSpec {
        override val route = "home"
        override val startRoute = ListsNavGraph
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(
            ListsNavGraph,
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
