package dev.alvr.katana.features.lists.ui.screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.ui.components.KatanaEmptyState
import dev.alvr.katana.core.ui.components.KatanaErrorState
import dev.alvr.katana.core.ui.components.home.KatanaHomeScaffold
import dev.alvr.katana.core.ui.components.home.rememberKatanaHomeScaffoldState
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.lists.ui.navigation.ListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar_search_placeholder
import dev.alvr.katana.features.lists.ui.resources.error_message
import dev.alvr.katana.features.lists.ui.resources.manga_toolbar_search_placeholder
import dev.alvr.katana.features.lists.ui.viewmodel.AnimeListsViewModel
import dev.alvr.katana.features.lists.ui.viewmodel.ListsViewModel
import dev.alvr.katana.features.lists.ui.viewmodel.MangaListsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ListScreen(
    viewModel: ListsViewModel<*, *>,
    navigator: ListsNavigator,
    title: String,
    emptyStateRes: String,
    backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.collectAsState()
    val katanaScaffoldState = rememberKatanaHomeScaffoldState()
    val lazyGridState = rememberLazyGridState()

    var showListSelector by remember { mutableStateOf(false) }

    ChangeListSheet(
        isVisible = showListSelector,
        lists = viewModel.userLists,
        selectedList = state.name.orEmpty(),
        onDismissRequest = { showListSelector = false },
        onClick = { name ->
            showListSelector = false
            viewModel.selectList(name).also {
                lazyGridState.scrollToItem(Int.zero)
                katanaScaffoldState.resetToolbar()
            }
        },
    )

    val searchPlaceholder = when (viewModel) {
        is AnimeListsViewModel -> Res.string.anime_toolbar_search_placeholder
        is MangaListsViewModel -> Res.string.manga_toolbar_search_placeholder
    }.value

    val buttonsVisible = !state.isError
    katanaScaffoldState.showTopAppBarActions = buttonsVisible

    KatanaHomeScaffold(
        katanaScaffoldState = katanaScaffoldState,
        title = title,
        subtitle = state.name,
        searchPlaceholder = searchPlaceholder,
        onSearch = viewModel::search,
        backContent = backContent,
        fab = {
            ChangeListButton(visible = buttonsVisible && viewModel.userLists.isNotEmpty()) {
                showListSelector = true
            }
        },
    ) { paddingValues ->
        when {
            state.isError -> KatanaErrorState(
                modifier = modifier.padding(paddingValues),
                text = Res.string.error_message.value,
                onRetry = {
                    viewModel.refreshList()
                    katanaScaffoldState.resetToolbar()
                },
                loading = state.isLoading,
            )
            state.isEmpty && !state.isLoading -> KatanaEmptyState(
                modifier = modifier.padding(paddingValues),
                text = emptyStateRes,
            )
            else -> MediaList(
                lazyGridState = lazyGridState,
                modifier = modifier.padding(paddingValues),
                listState = state,
                onRefresh = viewModel::refreshList,
                onAddPlusOne = viewModel::addPlusOne,
                onEditEntry = navigator::showEditEntry,
                onEntryDetails = navigator::navigateToEntryDetails,
            )
        }
    }
}
