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
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarItem
import dev.alvr.katana.core.ui.screens.KatanaScreen
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.navigation_bar_account
import dev.alvr.katana.features.home.ui.resources.navigation_bar_anime
import dev.alvr.katana.features.home.ui.resources.navigation_bar_explore
import dev.alvr.katana.features.home.ui.resources.navigation_bar_manga
import dev.alvr.katana.features.home.ui.resources.navigation_bar_social
import org.jetbrains.compose.resources.StringResource

internal enum class HomeNavigationBar(
    override val screen: KatanaScreen,
    override val selectedIcon: ImageVector,
    override val unselectedIcon: ImageVector,
    override val label: StringResource,
) : KatanaNavigationBarItem {
    AnimeList(
        screen = KatanaScreen.AnimeLists,
        selectedIcon = Icons.Filled.VideoLibrary,
        unselectedIcon = Icons.Outlined.VideoLibrary,
        label = Res.string.navigation_bar_anime,
    ),
    MangaList(
        screen = KatanaScreen.MangaLists,
        selectedIcon = Icons.AutoMirrored.Filled.LibraryBooks,
        unselectedIcon = Icons.AutoMirrored.Outlined.LibraryBooks,
        label = Res.string.navigation_bar_manga,
    ),
    Explore(
        screen = KatanaScreen.Explore,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        label = Res.string.navigation_bar_explore,
    ),
    Social(
        screen = KatanaScreen.Social,
        selectedIcon = Icons.Filled.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard,
        label = Res.string.navigation_bar_social,
    ),
    Account(
        screen = KatanaScreen.Account,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        label = Res.string.navigation_bar_account,
    ),
}
