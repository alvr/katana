package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.alvr.katana.core.ui.navigation.destinations.MainDestination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.empty_manga_list
import dev.alvr.katana.features.lists.ui.resources.manga_toolbar
import dev.alvr.katana.features.lists.ui.screens.components.ListScreen
import dev.alvr.katana.features.lists.ui.screens.components.NoItemSelectedPlaceholder
import dev.alvr.katana.features.lists.ui.viewmodel.MangaListsViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

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
        viewModel = metroViewModel<MangaListsViewModel>(),
        title = Res.string.manga_toolbar.value,
        emptyStateRes = Res.string.empty_manga_list.value,
        onEditEntry = {},
        onEntryDetails = {},
    )
}
