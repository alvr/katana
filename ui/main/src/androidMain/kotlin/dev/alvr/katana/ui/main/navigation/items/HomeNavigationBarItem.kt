package dev.alvr.katana.ui.main.navigation.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Dashboard
import androidx.compose.material.icons.twotone.Explore
import androidx.compose.material.icons.twotone.LibraryBooks
import androidx.compose.material.icons.twotone.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.ui.account.navigation.AccountNavGraph
import dev.alvr.katana.ui.explore.navigation.ExploreNavGraph
import dev.alvr.katana.ui.lists.navigation.AnimeNavGraph
import dev.alvr.katana.ui.lists.navigation.MangaNavGraph
import dev.alvr.katana.ui.main.R
import dev.alvr.katana.ui.social.navigation.SocialNavGraph
import kotlinx.collections.immutable.persistentListOf

internal enum class HomeNavigationBarItem(
    override val direction: NavGraphSpec,
    override val icon: ImageVector,
    override val label: Int,
) : NavigationBarItem {
    Anime(AnimeNavGraph, Icons.TwoTone.VideoLibrary, R.string.navigation_bar_destination_anime_lists),
    Manga(MangaNavGraph, Icons.TwoTone.LibraryBooks, R.string.navigation_bar_destination_manga_lists),
    Explore(ExploreNavGraph, Icons.TwoTone.Explore, R.string.navigation_bar_destination_explore),
    Social(SocialNavGraph, Icons.TwoTone.Dashboard, R.string.navigation_bar_destination_social),
    Account(AccountNavGraph, Icons.TwoTone.AccountCircle, R.string.navigation_bar_destination_account);

    companion object {
        @JvmField
        val values = persistentListOf(Anime, Manga, Explore, Social, Account)
    }
}
