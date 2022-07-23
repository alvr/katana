package dev.alvr.katana.ui.lists.view.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.view.components.MediaList
import dev.alvr.katana.ui.lists.viewmodel.manga.MangaListsState
import dev.alvr.katana.ui.lists.viewmodel.manga.MangaListsViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun MangaLists(
    vm: MangaListsViewModel = hiltViewModel(),
) {
    val state by vm.collectAsState()
    MangaLists(
        state = state,
        onRefresh = vm::fetchMangaLists,
    )
}

@Composable
private fun MangaLists(
    state: MangaListsState,
    onRefresh: () -> Unit,
) {
    MediaList(
        items = state.currentListItems,
        isEmpty = state.isEmpty,
        isLoading = state.isLoading,
        emptyStateRes = R.string.empty_manga_list,
        onRefresh = onRefresh,
    )
}
