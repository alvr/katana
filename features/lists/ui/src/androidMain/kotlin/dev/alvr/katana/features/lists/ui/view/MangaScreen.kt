package dev.alvr.katana.features.lists.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.features.lists.ui.navigation.ListsNavigator
import dev.alvr.katana.features.lists.ui.strings.LocalListsStrings
import dev.alvr.katana.features.lists.ui.view.components.ListScreen
import dev.alvr.katana.features.lists.ui.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.features.lists.ui.viewmodel.MangaListsViewModel
import org.koin.androidx.compose.koinViewModel

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
    val strings = LocalListsStrings.current

    ListScreen(
        vm = koinViewModel<MangaListsViewModel>(),
        navigator = navigator,
        resultRecipient = resultRecipient,
        title = strings.mangaToolbar,
        emptyStateRes = strings.emptyMangaList,
        backContent = { Filter() },
    )
}

@Composable
private fun Filter() {
    Text(text = "Manga Filter")
}
