package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import dev.alvr.katana.common.core.orEmpty
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

internal typealias Collection<T> = Map<String, List<T>>

internal abstract class ListsViewModel<E : MediaEntry, I : MediaListItem>(
    private val savedStateHandle: SavedStateHandle,
    private val updateListUseCase: UpdateListUseCase,
) : BaseViewModel<ListState<I>, Nothing>() {
    protected abstract val collectionFlow: Flow<MediaCollection<E>>

    override val container = container<ListState<I>, Nothing>(ListState()) {
        observeLists()
    }

    private var currentList: ImmutableList<I> = persistentListOf()

    val listNames get() = savedStateHandle.get<Array<String>>(LIST_NAMES) ?: emptyArray()

    protected abstract fun List<MediaListGroup<E>>.entryMap(): List<I>

    protected abstract fun observeListUseCase()

    fun refreshList() {
        updateState { copy(isLoading = true) }
        observeListUseCase()
    }

    fun addPlusOne(item: MediaListItem) {
        val entry = item.toMediaList().copy(progress = item.progress.inc())
        intent { updateListUseCase(entry) }
    }

    fun selectList(name: String) {
        val list = getListByName(name) ?: return
        currentList = list

        updateState {
            copy(
                items = list,
                name = name,
                isEmpty = list.isEmpty(),
            )
        }
    }

    fun search(search: String) {
        updateState {
            val filtered = currentList.filter { item ->
                item.title.contains(search, ignoreCase = true)
            }.toImmutableList()

            copy(
                items = filtered,
                isEmpty = filtered.isEmpty(),
            )
        }
    }

    private fun observeLists() {
        refreshList()
        collectCollectionFlow()
    }

    private fun collectCollectionFlow() {
        intent {
            collectionFlow.collect { collection ->
                val items = collection.lists
                    .groupBy { it.name }
                    .also { setListNames(it.keys.toTypedArray()) }
                    .mapValues { it.value.entryMap() }
                    .also { setCollection(it) }

                reduce {
                    val selectedListName = state.name ?: items.keys.firstOrNull()
                    val selectedList = getListByName(selectedListName).orEmpty()
                    currentList = selectedList

                    state.copy(
                        items = selectedList,
                        name = selectedListName,
                        isEmpty = selectedList.isEmpty(),
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun <T : MediaListItem> setCollection(items: Collection<T>) {
        savedStateHandle[COLLECTION] = items
    }

    private fun <T : MediaListItem> getCollection() =
        savedStateHandle.get<Collection<T>>(COLLECTION).orEmpty()

    private fun setListNames(names: Array<String>) {
        savedStateHandle[LIST_NAMES] = names
    }

    private fun <T : MediaListItem> Collection<T>.getListByName(name: String?) =
        get(name)?.toImmutableList()

    private fun getListByName(listName: String?) = getCollection<I>().getListByName(listName)

    companion object {
        private const val COLLECTION = "collection"
        private const val LIST_NAMES = "listNames"
    }
}
