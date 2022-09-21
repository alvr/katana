package dev.alvr.katana.ui.lists.view.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.destinations.ListSelectorDestination
import dev.alvr.katana.ui.lists.viewmodel.ListViewModel

@Composable
internal inline fun <reified VM : ListViewModel<*, *>> ListScreen(
    navigator: ListsNavigator,
    fromNavigator: ListsNavigator.From,
    resultRecipient: ResultRecipient<ListSelectorDestination, String>,
    @StringRes emptyStateRes: Int,
    vm: VM = hiltViewModel(),
) {
    val state by vm.container.stateFlow.collectAsState()

    resultRecipient.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> Unit
            is NavResult.Value -> vm.selectList(result.value)
        }
    }

    MediaList(
        listState = state,
        onRefresh = vm::refreshList,
        emptyStateRes = emptyStateRes,
        addPlusOne = vm::addPlusOne,
        editEntry = { navigator.openEditEntry(it, fromNavigator) },
        mediaDetails = { navigator.toMediaDetails(it, fromNavigator) },
    )
}
