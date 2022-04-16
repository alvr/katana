package dev.alvr.katana.ui.explore

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.alvr.katana.ui.base.components.home.HomeScaffold
import dev.alvr.katana.ui.base.components.home.HomeTopAppBar
import dev.alvr.katana.ui.explore.pages.Anime
import dev.alvr.katana.ui.explore.pages.Manga

@Preview
@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
fun Explore() {
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
