package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import co.touchlab.kermit.Logger
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.ui.navigation.ListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar
import dev.alvr.katana.features.lists.ui.resources.empty_anime_list
import dev.alvr.katana.features.lists.ui.screens.components.ListScreen
import dev.alvr.katana.features.lists.ui.viewmodel.AnimeListsViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
internal fun AnimeScreen(
    navigator: ListsNavigator,
) {
    ListScreen(
        viewModel = koinViewModel<AnimeListsViewModel>(),
        navigator = navigator,
        title = Res.string.anime_toolbar.value,
        emptyStateRes = Res.string.empty_anime_list.value,
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Anime Filter")
}
