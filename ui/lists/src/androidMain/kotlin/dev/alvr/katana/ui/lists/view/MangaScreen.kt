package dev.alvr.katana.ui.lists.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.components.ListScreen
import dev.alvr.katana.ui.lists.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.ui.lists.viewmodel.MangaListsViewModel
import katana.ui.lists.generated.resources.Res.string
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalResourceApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class,
)
internal fun MangaScreen(
    navigator: ListsNavigator,
    resultRecipient: ResultRecipient<ChangeListSheetDestination, String>,
) {
    ListScreen(
        vm = koinViewModel<MangaListsViewModel>(),
        navigator = navigator,
        resultRecipient = resultRecipient,
        title = stringResource(string.manga_toolbar),
        emptyStateRes = stringResource(string.empty_anime_list),
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Manga Filter")
}
