package dev.alvr.katana.features.account.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.features.account.ui.screens.AccountScreen

fun NavGraphBuilder.account(accountNavigator: AccountNavigator) {
    composable<HomeDestination.Account> {
        AccountScreen(accountNavigator)
    }
}
