package dev.alvr.katana.features.account.ui.navigation

import androidx.navigation.NavHostController
import dev.alvr.katana.core.ui.navigation.KatanaNavigator
import dev.alvr.katana.core.ui.navigation.overridden

interface AccountNavigator : KatanaNavigator

private class KatanaAccountNavigator(override val navController: NavHostController) : AccountNavigator {
    override fun navigateBack() {
        overridden()
    }
}

fun katanaAccountNavigator(navController: NavHostController): AccountNavigator = KatanaAccountNavigator(navController)
