package dev.alvr.katana.features.account.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.alvr.katana.features.account.ui.screens.AccountScreen

fun NavGraphBuilder.account(accountNavigator: AccountNavigator) {
    navigation<AccountDestination.Root>(startDestination = AccountDestination.Account) {
        composable<AccountDestination.Account> { AccountScreen(accountNavigator) }
    }
}
