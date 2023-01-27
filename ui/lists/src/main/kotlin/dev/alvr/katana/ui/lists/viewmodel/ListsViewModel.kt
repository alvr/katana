package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.Either
import dev.alvr.katana.common.core.orEmpty
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.UserList
import dev.alvr.katana.ui.lists.entities.mappers.toMediaList
import dev.alvr.katana.ui.lists.entities.mappers.toUserList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

internal typealias ListsCollection<T> = Map<String, List<T>>

internal abstract class ListsViewModel<E : MediaEntry, I : MediaListItem>(
    private val savedStateHandle: SavedStateHandle,
    private val updateListUseCase: UpdateListUseCase,
) : BaseViewModel<ListState<I>, Nothing>() {
    protected abstract val collectionFlow: Flow<Either<Failure, MediaCollection<E>>>

    override val container = container<ListState<I>, Nothing>(ListState()) {
        observeLists()
    }

    private var currentList: ImmutableList<I> = persistentListOf()

    val userLists get() = savedStateHandle.get<Array<UserList>>(USER_LISTS) ?: emptyArray()

    protected abstract fun List<MediaListGroup<E>>.entryMap(): List<I>

    protected abstract fun observeListUseCase()

    fun refreshList() {
        updateState { copy(isLoading = true) }
        observeListUseCase()
    }

    fun addPlusOne(id: Int) {
        val entry = with(currentList.first { it.entryId == id }.toMediaList()) {
            copy(progress = progress.inc())
        }
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
                collection.fold(
                    ifLeft = { onCollectCollectionError() },
                    ifRight = { media ->
                        val items = media.lists
                            .groupBy { it.name }
                            .mapValues { it.value.entryMap() }
                            .also { collection -> setCollection(collection) }

                        val selectedListName = state.name ?: items.keys.firstOrNull()
                        val selectedList = getListByName(selectedListName).orEmpty()
                        currentList = selectedList

                        reduce {
                            state.copy(
                                items = selectedList,
                                name = selectedListName,
                                isEmpty = selectedList.isEmpty(),
                                isLoading = false,
                                isError = false,
                            )
                        }
                    },
                )
            }
        }
    }

    private fun onCollectCollectionError() {
        updateState { copy(isError = true, isLoading = false, isEmpty = true) }
    }

    private fun <T : MediaListItem> setCollection(items: ListsCollection<T>) {
        savedStateHandle[COLLECTION] = items
        savedStateHandle[USER_LISTS] = items.toUserList()
    }

    private fun <T : MediaListItem> getCollection() =
        savedStateHandle.get<ListsCollection<T>>(COLLECTION).orEmpty()

    private fun <T : MediaListItem> ListsCollection<T>.getListByName(name: String?) =
        get(name)?.toImmutableList()

    private fun getListByName(listName: String?) = getCollection<I>().getListByName(listName)

    companion object {
        private const val COLLECTION = "collection"
        private const val USER_LISTS = "userLists"
    }
}
