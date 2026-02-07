package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.runtime.Composable
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.ui.navigation.AnimeListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar
import dev.alvr.katana.features.lists.ui.resources.empty_anime_list
import dev.alvr.katana.features.lists.ui.screens.components.ListScreen
import dev.alvr.katana.features.lists.ui.viewmodel.AnimeListsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun AnimeScreen(navigator: AnimeListsNavigator) {
    ListScreen(
        viewModel = koinViewModel<AnimeListsViewModel>(),
        title = Res.string.anime_toolbar.value,
        emptyStateRes = Res.string.empty_anime_list.value,
        onEditEntry = navigator::editAnimeEntry,
        onEntryDetails = navigator::animeEntryDetails,
    )
}
