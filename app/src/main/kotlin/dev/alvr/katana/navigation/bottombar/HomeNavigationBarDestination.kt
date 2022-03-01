package dev.alvr.katana.navigation.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Dashboard
import androidx.compose.material.icons.twotone.Explore
import androidx.compose.material.icons.twotone.FactCheck
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.R
import dev.alvr.katana.navigation.screens.Screen
import dev.alvr.katana.ui.base.navigation.NavigationBarDestination

internal enum class HomeNavigationBarDestination(
    @StringRes override val label: Int,
    override val icon: ImageVector,
    override val route: String
) : NavigationBarDestination {
    Lists(R.string.navigation_bar_destination_lists, Icons.TwoTone.FactCheck, Screen.Home.Lists.route),
    Explore(R.string.navigation_bar_destination_explore, Icons.TwoTone.Explore, Screen.Home.Explore.route),
    Social(R.string.navigation_bar_destination_social, Icons.TwoTone.Dashboard, Screen.Home.Social.route),
    Account(R.string.navigation_bar_destination_account, Icons.TwoTone.AccountCircle, Screen.Home.Account.route),
}
