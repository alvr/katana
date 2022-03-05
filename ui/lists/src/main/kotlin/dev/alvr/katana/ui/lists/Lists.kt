package dev.alvr.katana.ui.lists

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.alvr.katana.ui.base.components.home.HomeScaffold
import dev.alvr.katana.ui.base.components.home.HomeTopAppBar
import dev.alvr.katana.ui.lists.pages.AnimeList
import dev.alvr.katana.ui.lists.pages.MangaList

@Preview
@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
fun Lists() {
    HomeScaffold(
        tabs = enumValues<ListTabs>(),
        backContent = { /* TODO */ },
        pageContent = { page ->
            when (page) {
                ListTabs.Anime -> AnimeList()
                ListTabs.Manga -> MangaList()
            }
        }
    )
}

@Immutable
private enum class ListTabs(override val label: Int) : HomeTopAppBar {
    Anime(R.string.tab_anime),
    Manga(R.string.tab_manga),
}
