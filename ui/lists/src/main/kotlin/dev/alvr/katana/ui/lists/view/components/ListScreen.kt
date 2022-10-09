package dev.alvr.katana.ui.lists.view.components

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.destinations.ListSelectorDestination
import dev.alvr.katana.ui.lists.viewmodel.ListsViewModel

@Composable
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
internal inline fun <reified VM : ListsViewModel<*, *>> ListScreen(
    navigator: ListsNavigator,
    fromNavigator: ListsNavigator.From,
    resultRecipient: ResultRecipient<ListSelectorDestination, String>,
    @StringRes title: Int,
    @StringRes emptyStateRes: Int,
    noinline backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    vm: VM = hiltViewModel(),
) {
    val state by vm.container.stateFlow.collectAsState()

    resultRecipient.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> Unit
            is NavResult.Value -> vm.selectList(result.value)
        }
    }

    val searchPlaceholder = when (fromNavigator) {
        ListsNavigator.From.ANIME -> R.string.lists_toolbar_search_anime_placeholder
        ListsNavigator.From.MANGA -> R.string.lists_toolbar_search_manga_placeholder
    }.let { stringResource(it) }

    KatanaHomeScaffold(
        title = title,
        subtitle = state.name,
        searchPlaceholder = searchPlaceholder,
        search = state.search,
        onSearch = vm::search,
        backContent = backContent,
        fab = {
            ListSelectorButton {
                navigator.openListSelector(vm.listNames, fromNavigator)
            }
        },
    ) { paddingValues ->
        MediaList(
            modifier = modifier.padding(paddingValues),
            listState = state,
            onRefresh = vm::refreshList,
            emptyStateRes = emptyStateRes,
            addPlusOne = vm::addPlusOne,
            editEntry = { navigator.openEditEntry(it, fromNavigator) },
            mediaDetails = { navigator.toMediaDetails(it, fromNavigator) },
        )
    }
}
