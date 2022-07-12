package dev.alvr.katana.ui.lists.view.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.alvr.katana.ui.lists.view.components.MediaList
import dev.alvr.katana.ui.lists.viewmodel.manga.MangaListsState
import dev.alvr.katana.ui.lists.viewmodel.manga.MangaListsViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun MangaLists(
    vm: MangaListsViewModel = hiltViewModel(),
) {
    val state by vm.collectAsState()
    MangaLists(state)
}

@Composable
private fun MangaLists(state: MangaListsState) {
    MediaList(
        items = state.currentListItems,
        modifier = Modifier.fillMaxSize(),
    )
}
