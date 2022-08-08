package dev.alvr.katana.ui.lists.view.pages

import androidx.compose.runtime.Composable
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.view.components.MediaList
import dev.alvr.katana.ui.lists.viewmodel.ListsState

@Composable
internal fun AnimeLists(
    listState: ListsState.ListState<MediaListItem.AnimeListItem>,
    onRefresh: () -> Unit,
    addPlusOne: (MediaListItem) -> Unit,
    editEntry: (Int) -> Unit,
    mediaDetails: (Int) -> Unit,
) {
    MediaList(
        items = listState.items,
        isEmpty = listState.isEmpty,
        isLoading = listState.isLoading,
        emptyStateRes = R.string.empty_anime_list,
        onRefresh = onRefresh,
        addPlusOne = addPlusOne,
        editEntry = editEntry,
        mediaDetails = mediaDetails,
    )
}
