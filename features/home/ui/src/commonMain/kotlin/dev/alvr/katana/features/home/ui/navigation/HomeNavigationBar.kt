package dev.alvr.katana.features.home.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarItem
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.core.ui.utils.hasParentRoute
import dev.alvr.katana.core.ui.utils.hasRoute
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.navigation_bar_account
import dev.alvr.katana.features.home.ui.resources.navigation_bar_anime
import dev.alvr.katana.features.home.ui.resources.navigation_bar_explore
import dev.alvr.katana.features.home.ui.resources.navigation_bar_manga
import dev.alvr.katana.features.home.ui.resources.navigation_bar_social
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.StringResource

@Immutable
@Suppress("UseDataClass")
private class HomeNavigationBar(
    override val screen: HomeDestination,
    override val selectedIcon: ImageVector,
    override val unselectedIcon: ImageVector,
    override val label: StringResource,
) : HomeNavigationBarItem

interface HomeNavigationBarItem : KatanaNavigationBarItem {
    override val screen: HomeDestination

    companion object {
        fun <T : HomeNavigationBarItem> NavBackStackEntry?.hasRoute(screen: T) =
            hasRoute(screen.screen::class) || hasParentRoute(screen.screen::class)
    }
}

internal val homeNavigationBarItems: ImmutableList<HomeNavigationBarItem> = persistentListOf(
    HomeNavigationBar(
        screen = HomeDestination.AnimeLists,
        selectedIcon = Icons.Filled.VideoLibrary,
        unselectedIcon = Icons.Outlined.VideoLibrary,
        label = Res.string.navigation_bar_anime,
    ),
    HomeNavigationBar(
        screen = HomeDestination.MangaLists,
        selectedIcon = Icons.AutoMirrored.Filled.LibraryBooks,
        unselectedIcon = Icons.AutoMirrored.Outlined.LibraryBooks,
        label = Res.string.navigation_bar_manga,
    ),
    HomeNavigationBar(
        screen = HomeDestination.Explore,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        label = Res.string.navigation_bar_explore,
    ),
    HomeNavigationBar(
        screen = HomeDestination.Social,
        selectedIcon = Icons.Filled.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard,
        label = Res.string.navigation_bar_social,
    ),
    HomeNavigationBar(
        screen = HomeDestination.Account,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        label = Res.string.navigation_bar_account,
    ),
)
