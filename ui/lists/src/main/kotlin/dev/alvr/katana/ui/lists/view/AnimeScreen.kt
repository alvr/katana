package dev.alvr.katana.ui.lists.view

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
internal fun AnimeScreen(
    navigator: ListsNavigator,
    resultRecipient: ResultRecipient<ListSelectorDestination, String>,
) {
    ListScreen<AnimeListViewModel>(
        navigator = navigator,
        fromNavigator = ListsNavigator.From.ANIME,
        resultRecipient = resultRecipient,
        emptyStateRes = R.string.empty_anime_list,
    )
}
