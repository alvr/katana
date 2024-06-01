package dev.alvr.katana.features.lists.ui.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.Either
import dev.alvr.katana.core.common.orEmpty
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.entities.ListsCollection
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.UserList
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaList
import dev.alvr.katana.features.lists.ui.entities.mappers.toUserList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

internal sealed class ListsViewModel<E : MediaEntry, I : MediaListItem>(
    private val updateListUseCase: UpdateListUseCase,
) : BaseViewModel<ListState<I>, EmptyEffect>() {
    protected abstract val collectionFlow: Flow<Either<Failure, MediaCollection<E>>>

    override val container = viewModelScope.container<ListState<I>, EmptyEffect>(ListState()) {
        observeLists()
    }

    private var currentList: List<I> = emptyList()
    private var collection: ListsCollection<I> = emptyMap()

    var userLists: Array<UserList> = emptyArray()
        private set

    protected abstract fun List<MediaListGroup<E>>.entryMap(): List<I>

    protected abstract fun observeListUseCase()

    fun refreshList() {
        updateState { copy(isLoading = true) }
        observeListUseCase()
    }

    fun addPlusOne(id: Int) {
        val entry = currentList.first { it.entryId == id }.toMediaList().run {
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

    private fun setCollection(items: ListsCollection<I>) {
        collection = items
        userLists = items.toUserList()
    }

    private fun <T : MediaListItem> ListsCollection<T>.getListByName(name: String?) =
        get(name)?.toImmutableList()

    private fun getListByName(listName: String?) = collection.getListByName(listName)
}
