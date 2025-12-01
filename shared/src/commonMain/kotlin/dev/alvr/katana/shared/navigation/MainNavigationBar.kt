package dev.alvr.katana.shared.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.core.ui.navigation.KatanaDestination
import dev.alvr.katana.core.ui.navigation.KatanaNavigationBarItem
import dev.alvr.katana.core.ui.symbols.AccountCircle
import dev.alvr.katana.core.ui.symbols.AnimeLibrary
import dev.alvr.katana.core.ui.symbols.Explore
import dev.alvr.katana.core.ui.symbols.Home
import dev.alvr.katana.core.ui.symbols.KatanaSymbols
import dev.alvr.katana.core.ui.symbols.MangaLibrary
import dev.alvr.katana.features.account.ui.navigation.AccountDestination
import dev.alvr.katana.features.explore.ui.navigation.ExploreDestination
import dev.alvr.katana.features.home.ui.navigation.HomeDestination
import dev.alvr.katana.features.lists.ui.navigation.AnimeListsDestination
import dev.alvr.katana.features.lists.ui.navigation.MangaListsDestination
import dev.alvr.katana.shared.resources.Res
import dev.alvr.katana.shared.resources.navigation_bar_account
import dev.alvr.katana.shared.resources.navigation_bar_anime
import dev.alvr.katana.shared.resources.navigation_bar_explore
import dev.alvr.katana.shared.resources.navigation_bar_home
import dev.alvr.katana.shared.resources.navigation_bar_manga
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.StringResource

@Immutable
@Suppress("UseDataClass")
private class MainNavigationBar(
    override val screen: KatanaDestination,
    override val icon: ImageVector,
    override val label: StringResource,
    override val requireSession: Boolean,
) : MainNavigationBarItem {
    override fun toString(): String = label.key
}

internal interface MainNavigationBarItem : KatanaNavigationBarItem {
    val requireSession: Boolean
}

internal val mainNavigationBarItems: ImmutableList<MainNavigationBarItem> = persistentListOf(
    MainNavigationBar(
        screen = HomeDestination.Root,
        icon = KatanaSymbols.Home,
        label = Res.string.navigation_bar_home,
        requireSession = false,
    ),
    MainNavigationBar(
        screen = AnimeListsDestination.Root,
        icon = KatanaSymbols.AnimeLibrary,
        label = Res.string.navigation_bar_anime,
        requireSession = true,
    ),
    MainNavigationBar(
        screen = MangaListsDestination.Root,
        icon = KatanaSymbols.MangaLibrary,
        label = Res.string.navigation_bar_manga,
        requireSession = true,
    ),
    MainNavigationBar(
        screen = ExploreDestination.Root,
        icon = KatanaSymbols.Explore,
        label = Res.string.navigation_bar_explore,
        requireSession = false,
    ),
    MainNavigationBar(
        screen = AccountDestination.Root,
        icon = KatanaSymbols.AccountCircle,
        label = Res.string.navigation_bar_account,
        requireSession = false,
    ),
)
