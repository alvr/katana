package dev.alvr.katana.ui.lists.view

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.ui.base.components.home.HomeScaffold
import dev.alvr.katana.ui.base.components.home.HomeTopAppBar
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.components.ListSelectorButton
import dev.alvr.katana.ui.lists.view.components.MediaList
import dev.alvr.katana.ui.lists.view.destinations.ListSelectorDestination
import dev.alvr.katana.ui.lists.viewmodel.ListsViewModel
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
internal fun ListsScreen(
    navigator: ListsNavigator,
    resultRecipient: ResultRecipient<ListSelectorDestination, String>,
    vm: ListsViewModel = hiltViewModel(),
) {
    val tabs = ListTabs.values
    var selectedTab by remember { mutableStateOf(tabs.first()) }

    val state by vm.collectAsState()

    val subtitle = when (selectedTab) {
        ListTabs.Anime -> state.animeList.name
        ListTabs.Manga -> state.mangaList.name
    }

    resultRecipient.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> Unit
            is NavResult.Value -> when (selectedTab) {
                ListTabs.Anime -> vm.selectAnimeList(result.value)
                ListTabs.Manga -> vm.selectMangaList(result.value)
            }
        }
    }

    HomeScaffold(
        tabs = tabs,
        subtitle = subtitle,
        onSelectedTab = { selectedTab = it },
        backContent = { _, _ -> Text(text = "TODO") },
        pageContent = { page ->
            val (listState, emptyStateRes, onRefresh) = when (page) {
                ListTabs.Anime -> Triple(
                    state.animeList,
                    R.string.empty_anime_list,
                    vm::refreshAnimeLists,
                )
                ListTabs.Manga -> Triple(
                    state.mangaList,
                    R.string.empty_manga_list,
                    vm::refreshMangaLists,
                )
            }

            MediaList(
                listState = listState,
                onRefresh = onRefresh,
                emptyStateRes = emptyStateRes,
                addPlusOne = vm::addPlusOne,
                editEntry = navigator::openEditEntry,
                mediaDetails = navigator::toMediaDetails,
            )
        },
        fab = { page ->
            val lists = when (page) {
                ListTabs.Anime -> vm.animeListNames
                ListTabs.Manga -> vm.mangaListNames
            }

            ListSelectorButton {
                navigator.openListSelector(lists)
            }
        },
    )
}

@Immutable
private enum class ListTabs(override val label: Int) : HomeTopAppBar {
    Anime(R.string.tab_anime),
    Manga(R.string.tab_manga);

    companion object {
        @JvmField
        val values = persistentListOf(Anime, Manga)
    }
}
