package dev.alvr.katana.ui.lists.viewmodel

import androidx.annotation.CallSuper
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaList
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

internal abstract class ListsBaseViewModel<E : MediaEntry, I : MediaListItem>(
    private val updateListUseCase: UpdateListUseCase,
) : BaseViewModel<ListsState<I>, Nothing>() {
    protected abstract val listsFlow: Flow<MediaCollection<E>>

    override val container = container<ListsState<I>, Nothing>(ListsState()) {
        observeLists()
    }

    protected abstract fun List<MediaListGroup<E>>.listMapper(): List<I>

    @CallSuper
    open fun fetchLists() {
        updateState { copy(isLoading = true) }
    }

    fun addPlusOne(item: MediaListItem) {
        val entry = item.toMediaList().copy(progress = item.progress.inc())
        intent { updateListUseCase(entry) }
    }

    private fun observeLists() {
        fetchLists()

        intent {
            listsFlow.collect { active ->
                val items = active.lists.listMapper()

                reduce {
                    state.copy(
                        currentListItems = items,
                        isEmpty = items.isEmpty(),
                        isLoading = false,
                    )
                }
            }
        }
    }
}
