package dev.alvr.katana.features.lists.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.screens.HomeScreen
import dev.alvr.katana.features.lists.ui.navigation.ListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar
import dev.alvr.katana.features.lists.ui.resources.empty_anime_list
import dev.alvr.katana.features.lists.ui.screen.components.ListScreen
import dev.alvr.katana.features.lists.ui.viewmodel.AnimeListsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

internal fun NavGraphBuilder.animeLists(navigator: ListsNavigator) {
    composable<HomeScreen.AnimeLists> {
        AnimeScreen(navigator)
    }
}

@Composable
@OptIn(KoinExperimentalAPI::class)
private fun AnimeScreen(
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
