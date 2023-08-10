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
import dev.alvr.katana.ui.main.KR
import dev.alvr.katana.ui.social.navigation.SocialNavGraph
import kotlinx.collections.immutable.persistentListOf

internal enum class HomeNavigationBarItem(
    override val direction: NavGraphSpec,
    override val icon: ImageVector,
    override val label: String,
) : NavigationBarItem {
    Anime(AnimeNavGraph, Icons.TwoTone.VideoLibrary, KR.string.navigation_bar_destination_anime_lists),
    Manga(MangaNavGraph, Icons.TwoTone.LibraryBooks, KR.string.navigation_bar_destination_manga_lists),
    Explore(ExploreNavGraph, Icons.TwoTone.Explore, KR.string.navigation_bar_destination_explore),
    Social(SocialNavGraph, Icons.TwoTone.Dashboard, KR.string.navigation_bar_destination_social),
    Account(AccountNavGraph, Icons.TwoTone.AccountCircle, KR.string.navigation_bar_destination_account);

    companion object {
        @JvmField
        val values = persistentListOf(Anime, Manga, Explore, Social, Account)
    }
}
