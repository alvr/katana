package dev.alvr.katana.ui.lists.view.list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.components.ListScreen
import dev.alvr.katana.ui.lists.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.ui.lists.view.destinations.EditEntrySheetDestination
import dev.alvr.katana.ui.lists.viewmodel.AnimeListsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class,
)
internal fun AnimeScreen(
    navigator: ListsNavigator,
    changeListResult: ResultRecipient<ChangeListSheetDestination, String>,
    editEntryResult: ResultRecipient<EditEntrySheetDestination, MediaListItem>,
) {
    ListScreen(
        vm = koinViewModel<AnimeListsViewModel>(),
        navigator = navigator,
        title = R.string.lists_anime_toolbar_title,
        emptyStateRes = R.string.lists_empty_anime_list,
        changeListResult = changeListResult,
        editEntryResult = editEntryResult,
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Anime Filter")
}
