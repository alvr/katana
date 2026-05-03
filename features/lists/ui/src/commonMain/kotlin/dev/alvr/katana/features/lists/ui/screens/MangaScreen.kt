package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.alvr.katana.core.ui.navigation.destinations.MainDestination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.domain.models.lists.MediaListType
import dev.alvr.katana.features.lists.ui.navigation.MangaListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.empty_manga_list
import dev.alvr.katana.features.lists.ui.resources.manga_toolbar
import dev.alvr.katana.features.lists.ui.resources.manga_toolbar_search_placeholder
import dev.alvr.katana.features.lists.ui.screens.components.ListScreen
import dev.alvr.katana.features.lists.ui.screens.components.NoItemSelectedPlaceholder
import dev.alvr.katana.features.lists.ui.viewmodel.ListsViewModel
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun EntryProviderScope<KatanaDestination>.mangaLists() {
    entry<MainDestination.Manga>(
        metadata = ListDetailSceneStrategy.listPane(detailPlaceholder = { NoItemSelectedPlaceholder() })
    ) {
        MangaScreen()
    }
}

@Composable
private fun MangaScreen() {
    ListScreen(
        viewModel = assistedMetroViewModel<ListsViewModel, ListsViewModel.Factory> { create(MediaListType.Manga) },
        title = Res.string.manga_toolbar.value,
        searchPlaceholder = Res.string.manga_toolbar_search_placeholder.value,
        emptyState = Res.string.empty_manga_list.value,
        onEditEntry = {},
        onEntryDetails = {},
    )
}
