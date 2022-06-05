package dev.alvr.katana.navigation.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Dashboard
import androidx.compose.material.icons.twotone.Explore
import androidx.compose.material.icons.twotone.FactCheck
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.R
import dev.alvr.katana.ui.account.navigation.AccountNavGraph
import dev.alvr.katana.ui.explore.navigation.ExploreNavGraph
import dev.alvr.katana.ui.lists.navigation.ListsNavGraph
import dev.alvr.katana.ui.social.navigation.SocialNavGraph

internal sealed interface BottomBarDestinations {
    val direction: NavGraphSpec
    val icon: ImageVector
    @get:StringRes val label: Int
}

internal enum class HomeBottomBarDestinations(
    override val direction: NavGraphSpec,
    override val icon: ImageVector,
    override val label: Int,
) : BottomBarDestinations {
    Lists(ListsNavGraph, Icons.TwoTone.FactCheck, R.string.navigation_bar_destination_lists),
    Explore(ExploreNavGraph, Icons.TwoTone.Explore, R.string.navigation_bar_destination_explore),
    Social(SocialNavGraph, Icons.TwoTone.Dashboard, R.string.navigation_bar_destination_social),
    Account(AccountNavGraph, Icons.TwoTone.AccountCircle, R.string.navigation_bar_destination_account),
}
