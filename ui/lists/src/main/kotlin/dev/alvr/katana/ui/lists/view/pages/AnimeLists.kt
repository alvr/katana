package dev.alvr.katana.ui.lists.view.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.alvr.katana.ui.lists.view.components.MediaList
import dev.alvr.katana.ui.lists.viewmodel.anime.AnimeListsState
import dev.alvr.katana.ui.lists.viewmodel.anime.AnimeListsViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun AnimeLists(
    vm: AnimeListsViewModel = hiltViewModel(),
) {
    val state by vm.collectAsState()
    AnimeLists(state)
}

@Composable
private fun AnimeLists(state: AnimeListsState) {
    MediaList(
        items = state.currentListItems,
        modifier = Modifier.fillMaxSize(),
    )
}
