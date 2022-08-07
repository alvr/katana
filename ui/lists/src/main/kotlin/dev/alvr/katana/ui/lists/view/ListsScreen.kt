package dev.alvr.katana.ui.lists.view

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.ui.base.components.home.HomeScaffold
import dev.alvr.katana.ui.base.components.home.HomeTopAppBar
import dev.alvr.katana.ui.base.components.home.LocalHomeTopBarSubtitle
import dev.alvr.katana.ui.base.viewmodel.collectAsState
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.pages.AnimeLists
import dev.alvr.katana.ui.lists.view.pages.MangaLists
import dev.alvr.katana.ui.lists.viewmodel.ListsState
import dev.alvr.katana.ui.lists.viewmodel.ListsViewModel

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
internal fun Lists(
    navigator: ListsNavigator,
    vm: ListsViewModel = hiltViewModel(),
) {
    val tabs = enumValues<ListTabs>()
    var selectedTab by remember { mutableStateOf(tabs.first()) }

    val animeState by vm.collectAsState(ListsState::currentAnimeList)
    val mangaState by vm.collectAsState(ListsState::currentMangaList)

    val currentAnimeListName by vm.collectAsState(ListsState::currentAnimeListName)
    val currentMangaListName by vm.collectAsState(ListsState::currentMangaListName)

    val subtitle = when (selectedTab) {
        ListTabs.Anime -> currentAnimeListName
        ListTabs.Manga -> currentMangaListName
    }

    CompositionLocalProvider(LocalHomeTopBarSubtitle provides subtitle) {
        HomeScaffold(
            tabs = tabs,
            onSelectedTab = { selectedTab = it },
            backContent = { _, _ -> Text(text = "TODO") },
            pageContent = { page ->
                when (page) {
                    ListTabs.Anime -> AnimeLists(
                        state = animeState,
                        onRefresh = vm::fetchAnimeLists,
                        addPlusOne = vm::addPlusOne,
                        editEntry = navigator::openEditEntry,
                        mediaDetails = navigator::toMediaDetails,
                    )
                    ListTabs.Manga -> MangaLists(
                        state = mangaState,
                        onRefresh = vm::fetchMangaLists,
                        addPlusOne = vm::addPlusOne,
                        editEntry = navigator::openEditEntry,
                        mediaDetails = navigator::toMediaDetails,
                    )
                }
            },
        )
    }
}

@Immutable
private enum class ListTabs(override val label: Int) : HomeTopAppBar {
    Anime(R.string.tab_anime),
    Manga(R.string.tab_manga),
}
