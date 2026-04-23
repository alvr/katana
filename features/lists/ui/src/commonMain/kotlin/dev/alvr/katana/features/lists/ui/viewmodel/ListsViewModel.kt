package dev.alvr.katana.features.lists.ui.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.features.lists.domain.models.ItemEntryId
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.entities.ListEntries
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaList
import dev.zacsweers.metro.DefaultBinding
import dev.zacsweers.metro.ExperimentalMetroApi
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
@DefaultBinding<ViewModel>
@OptIn(ExperimentalMetroApi::class)
internal sealed class ListsViewModel<E : MediaEntry, I : MediaListItem>(
    dispatcher: KatanaDispatcher,
    type: ListsState.ListType,
    private val updateListUseCase: UpdateListUseCase,
) : KatanaViewModel<ListsState<I>, ListsEffect, ListsIntent>(dispatcher, ListsState(type)) {
    protected abstract val observeListUseCase: FlowEitherUseCase<Unit, MediaCollection<E>>

    private val searchFlow = MutableStateFlow(String.empty)

    protected abstract fun List<MediaListGroup<E>>.entryMap(): ListEntries<I>

    override fun init() {
        observeLists()
        observeSearch()
    }

    override fun handleIntent(intent: ListsIntent) {
        when (intent) {
            is ListsIntent.Refresh -> observeLists()
            is ListsIntent.AddPlusOne -> addPlusOne(intent.id)
            is ListsIntent.SelectList -> selectList(intent.name)
            is ListsIntent.Search -> search(intent.search)
        }
    }

    private fun observeLists() {
        state { copy(loading = true) }

        execute(
            useCase = observeListUseCase,
            params = Unit,
            onFailure = {
                state {
                    copy(collection = persistentMapOf(), selectedList = String.empty, error = true, loading = false)
                }
                effect(ListsEffect.LoadingListsFailure)
            },
            onSuccess = { media ->
                val collection = media.lists.groupBy { it.name }.mapValues { it.value.entryMap() }.toImmutableMap()

                state {
                    val selectedList = selectedList.ifEmpty { collection.keys.firstOrNull().orEmpty() }

                    copy(collection = collection, selectedList = selectedList, error = false, loading = false)
                }
            },
        )
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            searchFlow.debounce(SEARCH_DEBOUNCE).collect { query -> state { copy(searchQuery = query) } }
        }
    }

    private fun addPlusOne(id: ItemEntryId) {
        val listItem = currentState.entries[id] ?: return
        val entry = listItem.toMediaList().copy(progress = listItem.progress.inc())

        execute(
            useCase = updateListUseCase,
            params = entry,
            onSuccess = { effect(ListsEffect.AddPlusOneSuccess) },
            onFailure = { effect(ListsEffect.AddPlusOneFailure) },
        )
    }

    private fun selectList(name: String) {
        state { copy(selectedList = name) }
    }

    private fun search(search: String) {
        searchFlow.update { search }
    }
}

private const val SEARCH_DEBOUNCE = 250L
