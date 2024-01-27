package dev.alvr.katana.ui.lists.content

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.lists.component.MangaListComponent
import dev.alvr.katana.ui.lists.strings.LocalListsStrings

@Composable
fun MangaListContent(
    component: MangaListComponent,
    modifier: Modifier = Modifier,
) {
    val strings = LocalListsStrings.current

    ListContent(
        modifier = modifier,
        component = component,
        title = strings.mangaToolbar,
        emptyStateRes = strings.emptyMangaList,
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Manga Filter")
}
