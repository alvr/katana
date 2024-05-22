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
import dev.alvr.katana.shared.resources.Res
import dev.alvr.katana.shared.resources.navigation_bar_destination_account
import dev.alvr.katana.shared.resources.navigation_bar_destination_anime_lists
import dev.alvr.katana.shared.resources.navigation_bar_destination_explore
import dev.alvr.katana.shared.resources.navigation_bar_destination_manga_lists
import dev.alvr.katana.shared.resources.navigation_bar_destination_social
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.StringResource

internal enum class HomeNavigationBarItem(
    override val direction: NavGraphSpec,
    override val icon: ImageVector,
    override val label: StringResource,
) : NavigationBarItem {
    Anime(AnimeNavGraph, Icons.TwoTone.VideoLibrary, Res.string.navigation_bar_destination_anime_lists),
    Manga(MangaNavGraph, Icons.AutoMirrored.TwoTone.LibraryBooks, Res.string.navigation_bar_destination_manga_lists),
    Explore(ExploreNavGraph, Icons.TwoTone.Explore, Res.string.navigation_bar_destination_explore),
    Social(SocialNavGraph, Icons.TwoTone.Dashboard, Res.string.navigation_bar_destination_social),
    Account(AccountNavGraph, Icons.TwoTone.AccountCircle, Res.string.navigation_bar_destination_account);

    companion object {
        @JvmField
        val values = persistentListOf(Anime, Manga, Explore, Social, Account)
    }
}
