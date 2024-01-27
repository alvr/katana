package dev.alvr.katana.ui.lists.content

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.lists.component.AnimeListComponent
import dev.alvr.katana.ui.lists.strings.LocalListsStrings

@Composable
fun AnimeListContent(
    component: AnimeListComponent,
    modifier: Modifier = Modifier,
) {
    val strings = LocalListsStrings.current

    ListContent(
        modifier = modifier,
        component = component,
        title = strings.animeToolbar,
        emptyStateRes = strings.emptyAnimeList,
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Anime Filter")
}
