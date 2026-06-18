package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.runtime.Composable
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.ui.navigation.AnimeListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar_search_placeholder
import dev.alvr.katana.features.lists.ui.resources.empty_anime_list
import dev.alvr.katana.features.lists.ui.screens.components.ListScreen
import dev.alvr.katana.features.lists.ui.viewmodel.ListsViewModel
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
internal fun AnimeScreen(navigator: AnimeListsNavigator) {
    ListScreen(
        viewModel = assistedMetroViewModel<ListsViewModel, ListsViewModel.Factory> { create(MediaListType.Anime) },
        title = Res.string.anime_toolbar.value,
        searchPlaceholder = Res.string.anime_toolbar_search_placeholder.value,
        emptyState = Res.string.empty_anime_list.value,
        onEditEntry = navigator::editAnimeEntry,
        onEntryDetails = navigator::animeEntryDetails,
    )
}
