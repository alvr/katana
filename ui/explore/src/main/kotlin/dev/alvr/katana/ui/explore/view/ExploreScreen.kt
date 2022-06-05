package dev.alvr.katana.ui.explore.view

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.ui.base.components.home.HomeScaffold
import dev.alvr.katana.ui.base.components.home.HomeTopAppBar
import dev.alvr.katana.ui.explore.R
import dev.alvr.katana.ui.explore.view.pages.Anime
import dev.alvr.katana.ui.explore.view.pages.Manga

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
internal fun Explore() {
    HomeScaffold(
        tabs = enumValues<ExploreTabs>(),
        backContent = { },
        pageContent = { page ->
            when (page) {
                ExploreTabs.Anime -> Anime()
                ExploreTabs.Manga -> Manga()
            }
        },
    )
}

@Immutable
private enum class ExploreTabs(override val label: Int) : HomeTopAppBar {
    Anime(R.string.tab_anime),
    Manga(R.string.tab_manga),
}
