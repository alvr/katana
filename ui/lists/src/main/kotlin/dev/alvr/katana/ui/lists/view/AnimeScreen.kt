package dev.alvr.katana.ui.lists.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.components.ListScreen
import dev.alvr.katana.ui.lists.view.destinations.ListSelectorDestination
import dev.alvr.katana.ui.lists.viewmodel.AnimeListViewModel

@Composable
@Destination
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
internal fun AnimeScreen(
    navigator: ListsNavigator,
    resultRecipient: ResultRecipient<ListSelectorDestination, String>,
) {
    ListScreen<AnimeListViewModel>(
        navigator = navigator,
        fromNavigator = ListsNavigator.From.ANIME,
        resultRecipient = resultRecipient,
        title = R.string.anime_toolbar_title,
        emptyStateRes = R.string.empty_anime_list,
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Anime Filter")
}
