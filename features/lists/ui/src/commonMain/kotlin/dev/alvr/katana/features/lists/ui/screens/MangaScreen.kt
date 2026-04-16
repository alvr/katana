package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.runtime.Composable
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.ui.navigation.MangaListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.empty_manga_list
import dev.alvr.katana.features.lists.ui.resources.manga_toolbar
import dev.alvr.katana.features.lists.ui.screens.components.ListScreen
import dev.alvr.katana.features.lists.ui.viewmodel.MangaListsViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
internal fun MangaScreen(navigator: MangaListsNavigator) {
    ListScreen(
        viewModel = metroViewModel<MangaListsViewModel>(),
        title = Res.string.manga_toolbar.value,
        emptyStateRes = Res.string.empty_manga_list.value,
        onEditEntry = navigator::editMangaEntry,
        onEntryDetails = navigator::mangaEntryDetails,
    )
}
