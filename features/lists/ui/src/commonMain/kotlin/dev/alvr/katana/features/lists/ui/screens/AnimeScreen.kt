package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.Composable
import dev.alvr.katana.core.ui.navigation.KatanaEntryProviderInstaller
import dev.alvr.katana.core.ui.navigation.destinations.AnimeListsDestination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.domain.models.lists.MediaListType
import dev.alvr.katana.features.lists.ui.navigation.AnimeListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar_search_placeholder
import dev.alvr.katana.features.lists.ui.resources.empty_anime_list
import dev.alvr.katana.features.lists.ui.screens.components.ListScreen
import dev.alvr.katana.features.lists.ui.screens.components.NoItemSelectedPlaceholder
import dev.alvr.katana.features.lists.ui.viewmodel.ListsViewModel
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun animeLists(): KatanaEntryProviderInstaller = {
    entry<AnimeListsDestination.Root>(
        metadata = ListDetailSceneStrategy.listPane(detailPlaceholder = { NoItemSelectedPlaceholder() })
    ) { entry ->
        AnimeScreen()
    }
}

@Composable
private fun AnimeScreen() {
    ListScreen(
        viewModel = assistedMetroViewModel<ListsViewModel, ListsViewModel.Factory> { create(MediaListType.Anime) },
        title = Res.string.anime_toolbar.value,
        searchPlaceholder = Res.string.anime_toolbar_search_placeholder.value,
        emptyState = Res.string.empty_anime_list.value,
        onEditEntry = {},
        onEntryDetails = {},
    )
}
