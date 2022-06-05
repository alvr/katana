package dev.alvr.katana.ui.lists.view

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.ui.base.components.home.HomeScaffold
import dev.alvr.katana.ui.base.components.home.HomeTopAppBar
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.view.pages.AnimeList
import dev.alvr.katana.ui.lists.view.pages.MangaList

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
internal fun Lists() {
    HomeScaffold(
        tabs = enumValues<ListTabs>(),
        backContent = { },
        pageContent = { page ->
            when (page) {
                ListTabs.Anime -> AnimeList()
                ListTabs.Manga -> MangaList()
            }
        },
    )
}

@Immutable
private enum class ListTabs(override val label: Int) : HomeTopAppBar {
    Anime(R.string.tab_anime),
    Manga(R.string.tab_manga),
}
