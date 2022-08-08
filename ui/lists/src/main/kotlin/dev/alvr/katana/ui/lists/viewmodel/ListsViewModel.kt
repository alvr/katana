package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems
import dev.alvr.katana.ui.lists.entities.mappers.toMediaList
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

private typealias Collection<T> = Map<String, List<T>>

@HiltViewModel
internal class ListsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeAnimeListUseCase: ObserveAnimeListUseCase,
    private val observeMangaListUseCase: ObserveMangaListUseCase,
    private val updateListUseCase: UpdateListUseCase,
) : BaseViewModel<ListsState, Nothing>() {
    override val container = container<ListsState, Nothing>(ListsState()) {
        observeLists()
    }

    fun refreshAnimeLists() {
        updateState { copy(animeList = animeList.copy(isLoading = true)) }
        observeAnimeListUseCase()
    }

    fun refreshMangaLists() {
        updateState { copy(mangaList = mangaList.copy(isLoading = true)) }
        observeMangaListUseCase()
    }

    fun addPlusOne(item: MediaListItem) {
        val entry = item.toMediaList().copy(progress = item.progress.inc())
        intent { updateListUseCase(entry) }
    }

    private fun observeLists() {
        refreshAnimeLists()
        refreshMangaLists()

        collectAnimeLists()
        collectMangaLists()
    }

    private fun collectAnimeLists() {
        intent {
            observeAnimeListUseCase.flow.collect { collection ->
                val items = collection.lists
                    .groupBy { it.name }
                    .also { setListNames(ANIME_LIST_NAMES, it.keys.toTypedArray()) }
                    .mapValues { it.value.toMediaItems() }
                    .also { setCollection(ANIME_COLLECTION, it) }

                val defaultListName = items.keys.firstOrNull()
                val defaultList = items.getListByName(defaultListName)

                reduce {
                    state.copy(
                        animeList = state.animeList.copy(
                            items = defaultList,
                            name = defaultListName,
                            isEmpty = defaultList.isEmpty(),
                            isLoading = false,
                        ),
                    )
                }
            }
        }
    }

    private fun collectMangaLists() {
        intent {
            observeMangaListUseCase.flow.collect { collection ->
                val items = collection.lists
                    .groupBy { it.name }
                    .also { setListNames(MANGA_LIST_NAMES, it.keys.toTypedArray()) }
                    .mapValues { it.value.toMediaItems() }
                    .also { setCollection(MANGA_COLLECTION, it) }

                val defaultListName = items.keys.firstOrNull()
                val defaultList = items.getListByName(defaultListName)

                reduce {
                    state.copy(
                        mangaList = state.mangaList.copy(
                            items = defaultList,
                            name = defaultListName,
                            isEmpty = defaultList.isEmpty(),
                            isLoading = false,
                        ),
                    )
                }
            }
        }
    }

    private fun <T : MediaListItem> Map<String, List<T>>.getListByName(name: String?) =
        getOrElse(name.orEmpty()) { emptyList() }.toImmutableList()

    private fun <T : MediaListItem> setCollection(key: String, items: Collection<T>) {
        savedStateHandle[key] = items
    }

    private fun setListNames(key: String, names: Array<String>) {
        savedStateHandle[key] = names
    }

    companion object {
        private const val ANIME_COLLECTION = "animeCollection"
        private const val MANGA_COLLECTION = "mangaCollection"
        private const val ANIME_LIST_NAMES = "animeListNames"
        private const val MANGA_LIST_NAMES = "mangaListNames"
    }
}
