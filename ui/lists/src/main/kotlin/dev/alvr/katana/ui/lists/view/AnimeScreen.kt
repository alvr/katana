package dev.alvr.katana.ui.lists.view

import androidx.compose.animation.ExperimentalAnimationApi
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
import dev.alvr.katana.ui.lists.viewmodel.AnimeListsViewModel

@Composable
@Destination
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class,
)
internal fun AnimeScreen(
    navigator: ListsNavigator,
    resultRecipient: ResultRecipient<ListSelectorDestination, String>,
) {
    ListScreen<AnimeListsViewModel>(
        navigator = navigator,
        fromNavigator = ListsNavigator.From.ANIME,
        resultRecipient = resultRecipient,
        title = R.string.lists_anime_toolbar_title,
        emptyStateRes = R.string.lists_empty_anime_list,
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Anime Filter")
}
