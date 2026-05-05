package dev.alvr.katana.features.lists.ui.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import dev.alvr.katana.core.ui.components.KatanaEmptyState
import dev.alvr.katana.core.ui.components.KatanaErrorState
import dev.alvr.katana.core.ui.components.home.KatanaHomeScaffold
import dev.alvr.katana.core.ui.components.home.rememberKatanaHomeScaffoldState
import dev.alvr.katana.core.ui.navigation.LocalNavigator
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.viewmodel.CollectEffect
import dev.alvr.katana.core.ui.viewmodel.collectAsState
import dev.alvr.katana.features.lists.domain.models.ItemEntryId
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.navigation.ListsDestination
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.anime_toolbar_search_placeholder
import dev.alvr.katana.features.lists.ui.resources.error_message
import dev.alvr.katana.features.lists.ui.resources.manga_toolbar_search_placeholder
import dev.alvr.katana.features.lists.ui.screens.ChangeListButton
import dev.alvr.katana.features.lists.ui.viewmodel.ListsEffect
import dev.alvr.katana.features.lists.ui.viewmodel.ListsIntent
import dev.alvr.katana.features.lists.ui.viewmodel.ListsState
import dev.alvr.katana.features.lists.ui.viewmodel.ListsViewModel

@Composable
internal fun ListScreen(
    viewModel: ListsViewModel<out MediaEntry, out MediaListItem>,
    title: String,
    emptyStateRes: String,
    onEditEntry: (ItemEntryId) -> Unit,
    onEntryDetails: (ItemEntryId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val katanaScaffoldState = rememberKatanaHomeScaffoldState()
    val lazyGridState = rememberLazyGridState()

    val haptics = LocalHapticFeedback.current
    val navigator = LocalNavigator.current

    val state by viewModel.collectAsState()
    val onIntent by rememberUpdatedState(viewModel::intent)

    viewModel.CollectEffect { effect ->
        when (effect) {
            ListsEffect.AddPlusOneFailure -> TODO()
            ListsEffect.AddPlusOneSuccess -> haptics.performHapticFeedback(HapticFeedbackType.Confirm)
            ListsEffect.LoadingListsFailure -> TODO()
        }
    }

    // TODO: Listen to results

    val searchPlaceholder =
        when (state.type) {
            ListsState.ListType.Anime -> Res.string.anime_toolbar_search_placeholder
            ListsState.ListType.Manga -> Res.string.manga_toolbar_search_placeholder
        }.value

    val buttonsVisible = !state.error

    KatanaHomeScaffold(
        katanaScaffoldState = katanaScaffoldState,
        title = title,
        subtitle = state.selectedList,
        searchPlaceholder = searchPlaceholder,
        onSearch = { search -> onIntent(ListsIntent.Search(search)) },
        fab = {
            ChangeListButton(visible = buttonsVisible && state.lists.isNotEmpty()) {
                navigator.add(ListsDestination.Selector(selected = state.selectedList, lists = state.lists))
            }
        },
    ) { paddingValues ->
        when {
            state.error ->
                KatanaErrorState(
                    modifier = modifier.padding(paddingValues),
                    text = Res.string.error_message.value,
                    onRetry = {
                        onIntent(ListsIntent.Refresh)
                        katanaScaffoldState.resetToolbar()
                    },
                    loading = state.loading,
                )
            state.empty && !state.loading ->
                KatanaEmptyState(modifier = modifier.padding(paddingValues), text = emptyStateRes)
            else ->
                MediaList(
                    lazyGridState = lazyGridState,
                    modifier = modifier.padding(paddingValues),
                    items = state.items,
                    loading = state.loading,
                    onRefresh = { onIntent(ListsIntent.Refresh) },
                    onAddPlusOne = { entryId -> onIntent(ListsIntent.AddPlusOne(entryId)) },
                    onEditEntry = onEditEntry,
                    onEntryDetails = onEntryDetails,
                )
        }
    }
}
