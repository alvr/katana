package dev.alvr.katana.navigation

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.home.navigation.HomeNavGraph
import dev.alvr.katana.ui.login.navigation.LoginNavGraph

internal object NavGraph {
    val root = object : NavGraphSpec {
        override val route = "root"
        override val startRoute = LoginNavGraph
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(LoginNavGraph, HomeNavGraph)
    }
}
