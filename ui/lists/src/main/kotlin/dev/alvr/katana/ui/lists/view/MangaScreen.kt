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
import dev.alvr.katana.ui.lists.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.ui.lists.viewmodel.MangaListsViewModel

@Composable
@Destination
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class,
)
internal fun MangaScreen(
    navigator: ListsNavigator,
    resultRecipient: ResultRecipient<ChangeListSheetDestination, String>,
) {
    ListScreen<MangaListsViewModel>(
        navigator = navigator,
        fromNavigator = ListsNavigator.From.MANGA,
        resultRecipient = resultRecipient,
        title = R.string.lists_manga_toolbar_title,
        emptyStateRes = R.string.lists_empty_manga_list,
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Manga Filter")
}
