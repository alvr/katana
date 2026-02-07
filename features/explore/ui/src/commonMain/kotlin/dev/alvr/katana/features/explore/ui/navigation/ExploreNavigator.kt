package dev.alvr.katana.features.explore.ui.navigation

import androidx.navigation.NavHostController
import dev.alvr.katana.core.ui.navigation.KatanaNavigator
import dev.alvr.katana.core.ui.navigation.overridden

interface ExploreNavigator : KatanaNavigator

private class KatanaExploreNavigator(override val navController: NavHostController) : ExploreNavigator {
    override fun navigateBack() {
        overridden()
    }
}

fun katanaExploreNavigator(navController: NavHostController): ExploreNavigator = KatanaExploreNavigator(navController)
