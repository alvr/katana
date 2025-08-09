package dev.alvr.katana.features.lists.ui.viewmodel

import androidx.compose.runtime.Stable
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
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap

@Stable
internal sealed class ListsViewModel<E : MediaEntry, I : MediaListItem>(
    type: ListsState.ListType,
    private val updateListUseCase: UpdateListUseCase,
) : KatanaViewModel<ListsState<I>, ListsEffect, ListsIntent>(ListsState(type)) {
    protected abstract val observeListUseCase: FlowEitherUseCase<Unit, MediaCollection<E>>

    protected abstract fun List<MediaListGroup<E>>.entryMap(): ListEntries<I>

    override fun init() {
        observeLists()
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
                    copy(
                        collection = persistentMapOf(),
                        selectedList = String.empty,
                        error = true,
                        loading = false,
                    )
                }
                effect(ListsEffect.LoadingListsFailure)
            },
            onSuccess = { media ->
                val collection = media.lists
                    .groupBy { it.name }
                    .mapValues { it.value.entryMap() }
                    .toImmutableMap()

                state {
                    val selectedList = selectedList.ifEmpty { collection.keys.firstOrNull().orEmpty() }

                    copy(
                        collection = collection,
                        selectedList = selectedList,
                        error = false,
                        loading = false,
                    )
                }
            },
        )
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
        state {
            copy(
                selectedList = name,
            )
        }
    }

    private fun search(search: String) {
        state {
            copy(searchQuery = search)
        }
    }
}
