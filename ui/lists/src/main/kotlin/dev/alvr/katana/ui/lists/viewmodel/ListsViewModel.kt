package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.common.core.orEmpty
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

internal typealias Collection<T> = Map<String, List<T>>

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

    val animeListNames get() = getListNames(ANIME_LIST_NAMES)
    val mangaListNames get() = getListNames(MANGA_LIST_NAMES)

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

    fun selectAnimeList(name: String) {
        val list = getAnimeList(name) ?: return

        updateState {
            copy(
                animeList = animeList.copy(
                    items = list,
                    name = name,
                    isEmpty = list.isEmpty(),
                ),
            )
        }
    }

    fun selectMangaList(name: String) {
        val list = getMangaList(name) ?: return

        updateState {
            copy(
                mangaList = mangaList.copy(
                    items = list,
                    name = name,
                    isEmpty = list.isEmpty(),
                ),
            )
        }
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

                reduce {
                    val selectedListName = state.animeList.name ?: items.keys.firstOrNull()
                    val selectedList = getAnimeList(selectedListName).orEmpty()

                    state.copy(
                        animeList = state.animeList.copy(
                            items = selectedList,
                            name = selectedListName,
                            isEmpty = selectedList.isEmpty(),
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

                reduce {
                    val selectedListName = state.mangaList.name ?: items.keys.firstOrNull()
                    val selectedList = getMangaList(selectedListName).orEmpty()

                    state.copy(
                        mangaList = state.mangaList.copy(
                            items = selectedList,
                            name = selectedListName,
                            isEmpty = selectedList.isEmpty(),
                            isLoading = false,
                        ),
                    )
                }
            }
        }
    }

    private fun <T : MediaListItem> setCollection(key: String, items: Collection<T>) {
        savedStateHandle[key] = items
    }

    private fun <T : MediaListItem> getCollection(key: String) =
        savedStateHandle.get<Collection<T>>(key).orEmpty()

    private fun setListNames(key: String, names: Array<String>) {
        savedStateHandle[key] = names
    }

    private fun getListNames(key: String): Array<String> =
        savedStateHandle.get<Array<String>>(key) ?: emptyArray()

    private fun <T : MediaListItem> Collection<T>.getListByName(name: String?) =
        get(name)?.toImmutableList()

    private fun getAnimeList(listName: String?) =
        getCollection<MediaListItem.AnimeListItem>(ANIME_COLLECTION).getListByName(listName)

    private fun getMangaList(listName: String?) =
        getCollection<MediaListItem.MangaListItem>(MANGA_COLLECTION).getListByName(listName)

    companion object {
        private const val ANIME_COLLECTION = "animeCollection"
        private const val MANGA_COLLECTION = "mangaCollection"
        private const val ANIME_LIST_NAMES = "animeListNames"
        private const val MANGA_LIST_NAMES = "mangaListNames"
    }
}
