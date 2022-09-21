package dev.alvr.katana.ui.lists.view

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.components.ListScreen
import dev.alvr.katana.ui.lists.view.destinations.ListSelectorDestination
import dev.alvr.katana.ui.lists.viewmodel.MangaListViewModel

@Composable
@Destination
internal fun MangaScreen(
    navigator: ListsNavigator,
    resultRecipient: ResultRecipient<ListSelectorDestination, String>,
) {
    ListScreen<MangaListViewModel>(
        navigator = navigator,
        fromNavigator = ListsNavigator.From.MANGA,
        resultRecipient = resultRecipient,
        emptyStateRes = R.string.empty_manga_list,
    )
}
