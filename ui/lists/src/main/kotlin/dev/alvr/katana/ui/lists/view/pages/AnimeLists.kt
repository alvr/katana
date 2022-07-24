package dev.alvr.katana.ui.lists.view.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.view.components.MediaList
import dev.alvr.katana.ui.lists.viewmodel.ListsState
import dev.alvr.katana.ui.lists.viewmodel.anime.AnimeListsViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun AnimeLists(
    vm: AnimeListsViewModel = hiltViewModel(),
) {
    val state by vm.collectAsState()
    AnimeLists(
        state = state,
        onRefresh = vm::fetchLists,
        addPlusOne = vm::addPlusOne,
    )
}

@Composable
private fun AnimeLists(
    state: ListsState<MediaListItem.AnimeListItem>,
    onRefresh: () -> Unit,
    addPlusOne: (MediaListItem) -> Unit,
) {
    MediaList(
        items = state.currentListItems,
        isEmpty = state.isEmpty,
        isLoading = state.isLoading,
        emptyStateRes = R.string.empty_anime_list,
        onRefresh = onRefresh,
        addPlusOne = addPlusOne,
    )
}
