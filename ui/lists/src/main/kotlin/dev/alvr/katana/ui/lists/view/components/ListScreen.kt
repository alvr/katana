package dev.alvr.katana.ui.lists.view.components

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.base.components.home.rememberKatanaHomeScaffoldState
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.destinations.ChangeListSheetDestination
import dev.alvr.katana.ui.lists.viewmodel.ListsViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
internal inline fun <reified VM : ListsViewModel<*, *>> ListScreen(
    navigator: ListsNavigator,
    fromNavigator: ListsNavigator.From,
    resultRecipient: ResultRecipient<ChangeListSheetDestination, String>,
    @StringRes title: Int,
    @StringRes emptyStateRes: Int,
    noinline backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    vm: ListsViewModel<*, *> = hiltViewModel<VM>(),
) {
    val state by vm.collectAsState()
    val katanaScaffoldState = rememberKatanaHomeScaffoldState()
    val lazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    resultRecipient.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> Unit
            is NavResult.Value -> vm.selectList(result.value).also {
                coroutineScope.launch { lazyGridState.scrollToItem(Int.zero) }
                katanaScaffoldState.resetToolbar()
            }
        }
    }

    val searchPlaceholder = when (fromNavigator) {
        ListsNavigator.From.ANIME -> R.string.lists_toolbar_search_anime_placeholder
        ListsNavigator.From.MANGA -> R.string.lists_toolbar_search_manga_placeholder
    }.let { stringResource(it) }

    KatanaHomeScaffold(
        katanaScaffoldState = katanaScaffoldState,
        title = title,
        subtitle = state.name,
        searchPlaceholder = searchPlaceholder,
        onSearch = vm::search,
        backContent = backContent,
        fab = {
            ChangeListButton {
                navigator.openListSelector(vm.listNames, fromNavigator)
            }
        },
    ) { paddingValues ->
        MediaList(
            lazyGridState = lazyGridState,
            modifier = modifier.padding(paddingValues),
            listState = state,
            onRefresh = vm::refreshList,
            addPlusOne = vm::addPlusOne,
            editEntry = { navigator.openEditEntry(it, fromNavigator) },
            mediaDetails = { navigator.toMediaDetails(it, fromNavigator) },
        )
    }
}
