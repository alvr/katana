package dev.alvr.katana.ui.lists.view.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.ui.base.OnNavValue
import dev.alvr.katana.ui.base.components.KatanaEmptyState
import dev.alvr.katana.ui.base.components.KatanaErrorState
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.base.components.home.rememberKatanaHomeScaffoldState
import dev.alvr.katana.ui.base.viewmodel.collectAsState
import dev.alvr.katana.ui.lists.lists.generated.resources.Res
import dev.alvr.katana.ui.lists.lists.generated.resources.anime_toolbar_search_placeholder
import dev.alvr.katana.ui.lists.lists.generated.resources.error_message
import dev.alvr.katana.ui.lists.lists.generated.resources.error_retry_button
import dev.alvr.katana.ui.lists.lists.generated.resources.manga_toolbar_search_placeholder
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.ui.lists.viewmodel.AnimeListsViewModel
import dev.alvr.katana.ui.lists.viewmodel.ListsViewModel
import dev.alvr.katana.ui.lists.viewmodel.MangaListsViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalResourceApi::class,
    ExperimentalAnimationApi::class,
)
internal fun ListScreen(
    vm: ListsViewModel<*, *>,
    navigator: ListsNavigator,
    resultRecipient: ResultRecipient<ChangeListSheetDestination, String>,
    title: String,
    emptyStateRes: String,
    backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by vm.collectAsState()
    val katanaScaffoldState = rememberKatanaHomeScaffoldState()
    val lazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    resultRecipient.OnNavValue { result ->
        vm.selectList(result).also {
            coroutineScope.launch { lazyGridState.scrollToItem(Int.zero) }
            katanaScaffoldState.resetToolbar()
        }
    }

    val searchPlaceholder = when (vm) {
        is AnimeListsViewModel -> Res.string.anime_toolbar_search_placeholder
        is MangaListsViewModel -> Res.string.manga_toolbar_search_placeholder
    }.let { res -> stringResource(res) }

    val buttonsVisible by remember(state.isError) {
        derivedStateOf(structuralEqualityPolicy()) { !state.isError }
    }
    katanaScaffoldState.showTopAppBarActions = buttonsVisible

    KatanaHomeScaffold(
        katanaScaffoldState = katanaScaffoldState,
        title = title,
        subtitle = state.name,
        searchPlaceholder = searchPlaceholder,
        onSearch = vm::search,
        backContent = backContent,
        fab = {
            ChangeListButton(visible = buttonsVisible && vm.userLists.isNotEmpty()) {
                navigator.listSelector(vm.userLists, state.name.orEmpty())
            }
        },
    ) { paddingValues ->
        with(state) {
            when {
                isError -> KatanaErrorState(
                    modifier = modifier.padding(paddingValues),
                    text = stringResource(Res.string.error_message),
                    onRetry = {
                        vm.refreshList()
                        katanaScaffoldState.resetToolbar()
                    },
                    buttonText = stringResource(Res.string.error_retry_button),
                    loading = state.isLoading,
                )
                isEmpty && !isLoading -> KatanaEmptyState(
                    modifier = modifier.padding(paddingValues),
                    text = emptyStateRes,
                )
                else -> MediaList(
                    lazyGridState = lazyGridState,
                    modifier = modifier.padding(paddingValues),
                    listState = state,
                    onRefresh = vm::refreshList,
                    onAddPlusOne = vm::addPlusOne,
                    onEditEntry = navigator::editEntry,
                    onEntryDetails = navigator::entryDetails,
                )
            }
        }
    }
}
