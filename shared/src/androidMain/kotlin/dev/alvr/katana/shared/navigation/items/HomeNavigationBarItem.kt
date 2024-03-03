package dev.alvr.katana.shared.navigation.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.LibraryBooks
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Dashboard
import androidx.compose.material.icons.twotone.Explore
import androidx.compose.material.icons.twotone.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dev.alvr.katana.features.account.ui.navigation.AccountNavGraph
import dev.alvr.katana.features.explore.ui.navigation.ExploreNavGraph
import dev.alvr.katana.features.lists.ui.navigation.AnimeNavGraph
import dev.alvr.katana.features.lists.ui.navigation.MangaNavGraph
import dev.alvr.katana.features.social.ui.navigation.SocialNavGraph
import dev.alvr.katana.shared.R
import kotlinx.collections.immutable.persistentListOf

internal enum class HomeNavigationBarItem(
    override val direction: NavGraphSpec,
    override val icon: ImageVector,
    override val label: Int,
) : NavigationBarItem {
    Anime(AnimeNavGraph, Icons.TwoTone.VideoLibrary, R.string.navigation_bar_destination_anime_lists),
    Manga(MangaNavGraph, Icons.AutoMirrored.TwoTone.LibraryBooks, R.string.navigation_bar_destination_manga_lists),
    Explore(ExploreNavGraph, Icons.TwoTone.Explore, R.string.navigation_bar_destination_explore),
    Social(SocialNavGraph, Icons.TwoTone.Dashboard, R.string.navigation_bar_destination_social),
    Account(AccountNavGraph, Icons.TwoTone.AccountCircle, R.string.navigation_bar_destination_account);

    companion object {
        @JvmField
        val values = persistentListOf(Anime, Manga, Explore, Social, Account)
    }
}
