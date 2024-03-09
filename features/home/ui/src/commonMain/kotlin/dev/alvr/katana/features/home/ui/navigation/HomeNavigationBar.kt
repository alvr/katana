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
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.core.ui.components.navigation.KatanaNavigationBarItem
import dev.alvr.katana.features.home.ui.component.DefaultHomeComponent.Config

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
        override val label @Composable get() = "Anime" // LocalHomeStrings.current.animeListNav
    },
    MangaList(
        key = Config.MangaList::class,
        selectedIcon = Icons.AutoMirrored.Filled.LibraryBooks,
        unselectedIcon = Icons.AutoMirrored.Outlined.LibraryBooks,
    ) {
        override val label @Composable get() = "Manga" // LocalHomeStrings.current.mangaListNav
    },
    Explore(
        key = Config.Explore::class,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
    ) {
        override val label @Composable get() = "Explore" // LocalHomeStrings.current.exploreListNav
    },
    Social(
        key = Config.Social::class,
        selectedIcon = Icons.Filled.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard,
    ) {
        override val label @Composable get() = "Social" // LocalHomeStrings.current.socialListNav
    },
    Account(
        key = Config.Account::class,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
    ) {
        override val label @Composable get() = "Account" // LocalHomeStrings.current.accountListNav
    }
}
