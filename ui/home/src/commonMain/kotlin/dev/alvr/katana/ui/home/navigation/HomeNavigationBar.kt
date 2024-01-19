package dev.alvr.katana.ui.home.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.ui.base.components.navigation.KatanaNavigationBarItem
import dev.alvr.katana.ui.home.component.DefaultHomeComponent.Config
import dev.alvr.katana.ui.home.strings.LocalHomeStrings

internal enum class HomeNavigationBar(
    override val key: Any,
    override val selectedIcon: ImageVector,
    override val unselectedIcon: ImageVector,
) : KatanaNavigationBarItem {
    AnimeList(
        key = Config.AnimeList::class,
        selectedIcon = Icons.Filled.VideoLibrary,
        unselectedIcon = Icons.Outlined.VideoLibrary,
    ) {
        override val label @Composable get() = LocalHomeStrings.current.animeListNav
    },
    MangaList(
        key = Config.MangaList::class,
        selectedIcon = Icons.Filled.LibraryBooks,
        unselectedIcon = Icons.Outlined.LibraryBooks,
    ) {
        override val label @Composable get() = LocalHomeStrings.current.mangaListNav
    },
    Explore(
        key = Config.Explore::class,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
    ) {
        override val label @Composable get() = LocalHomeStrings.current.exploreListNav
    },
    Social(
        key = Config.Social::class,
        selectedIcon = Icons.Filled.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard,
    ) {
        override val label @Composable get() = LocalHomeStrings.current.socialListNav
    },
    Account(
        key = Config.Account::class,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
    ) {
        override val label @Composable get() = LocalHomeStrings.current.accountListNav
    }
}
