package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.alvr.katana.core.ui.navigation.destinations.MainDestination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar
import dev.alvr.katana.features.lists.ui.resources.empty_anime_list
import dev.alvr.katana.features.lists.ui.screens.components.ListScreen
import dev.alvr.katana.features.lists.ui.screens.components.NoItemSelectedPlaceholder
import dev.alvr.katana.features.lists.ui.viewmodel.AnimeListsViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun EntryProviderScope<KatanaDestination>.animeLists() {
    entry<MainDestination.Anime>(
        metadata = ListDetailSceneStrategy.listPane(detailPlaceholder = { NoItemSelectedPlaceholder() })
    ) {
        AnimeScreen()
    }
}

@Composable
private fun AnimeScreen() {
    ListScreen(
        viewModel = metroViewModel<AnimeListsViewModel>(),
        title = Res.string.anime_toolbar.value,
        emptyStateRes = Res.string.empty_anime_list.value,
        onEditEntry = {},
        onEntryDetails = {},
    )
}
