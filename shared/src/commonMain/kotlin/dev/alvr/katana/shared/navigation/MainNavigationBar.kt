package dev.alvr.katana.shared.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.core.ui.navigation.KatanaNavigationBarItem
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.alvr.katana.core.ui.navigation.destinations.TopLevelDestination
import dev.alvr.katana.core.ui.symbols.AccountCircle
import dev.alvr.katana.core.ui.symbols.AnimeLibrary
import dev.alvr.katana.core.ui.symbols.Explore
import dev.alvr.katana.core.ui.symbols.Home
import dev.alvr.katana.core.ui.symbols.KatanaSymbols
import dev.alvr.katana.core.ui.symbols.MangaLibrary
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

internal val mainNavigationBarItems: ImmutableList<MainNavigationBarItem> =
    persistentListOf(
        MainNavigationBar(
            screen = TopLevelDestination.Home(),
            icon = KatanaSymbols.Home,
            label = Res.string.navigation_bar_home,
            requireSession = false,
        ),
        MainNavigationBar(
            screen = TopLevelDestination.Anime,
            icon = KatanaSymbols.AnimeLibrary,
            label = Res.string.navigation_bar_anime,
            requireSession = true,
        ),
        MainNavigationBar(
            screen = TopLevelDestination.Manga,
            icon = KatanaSymbols.MangaLibrary,
            label = Res.string.navigation_bar_manga,
            requireSession = true,
        ),
        MainNavigationBar(
            screen = TopLevelDestination.Explore,
            icon = KatanaSymbols.Explore,
            label = Res.string.navigation_bar_explore,
            requireSession = false,
        ),
        MainNavigationBar(
            screen = TopLevelDestination.Account,
            icon = KatanaSymbols.AccountCircle,
            label = Res.string.navigation_bar_account,
            requireSession = false,
        ),
    )
