package dev.alvr.katana.ui.lists.viewmodel

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
import kotlinx.collections.immutable.toImmutableMap
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class ListsViewModel @Inject constructor(
    private val observeAnimeListUseCase: ObserveAnimeListUseCase,
    private val observeMangaListUseCase: ObserveMangaListUseCase,
    private val updateListUseCase: UpdateListUseCase,
) : BaseViewModel<ListsState, Nothing>() {
    override val container = container<ListsState, Nothing>(ListsState()) {
        observeLists()
    }

    fun fetchAnimeLists() {
        updateState { copy(currentAnimeList = currentAnimeList.copy(isLoading = true)) }
        observeAnimeListUseCase()
    }

    fun fetchMangaLists() {
        updateState { copy(currentMangaList = currentMangaList.copy(isLoading = true)) }
        observeMangaListUseCase()
    }

    fun addPlusOne(item: MediaListItem) {
        val entry = item.toMediaList().copy(progress = item.progress.inc())
        intent { updateListUseCase(entry) }
    }

    private fun observeLists() {
        fetchAnimeLists()
        fetchMangaLists()

        collectAnimeLists()
        collectMangaLists()
    }

    private fun collectAnimeLists() {
        intent {
            observeAnimeListUseCase.flow.collect { collection ->
                val animeListNames = mutableListOf<String>()
                val items = collection.lists
                    .groupBy { it.name }
                    .also { animeListNames.addAll(it.keys) }
                    .mapValues { it.value.toMediaItems() }
                    .toImmutableMap()

                reduce {
                    val selectedAnimeList = if (state.currentAnimeListName.isNullOrEmpty()) {
                        items.keys.firstOrNull()
                    } else {
                        state.currentAnimeListName
                    }

                    val defaultList = items.defaultList(selectedAnimeList)

                    state.copy(
                        currentAnimeListName = selectedAnimeList,
                        animeCollection = items,
                        animeListNames = animeListNames.toImmutableList(),
                        currentAnimeList = state.currentAnimeList.copy(
                            items = defaultList,
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
                val mangaListNames = mutableListOf<String>()
                val items = collection.lists
                    .groupBy { it.name }
                    .also { mangaListNames.addAll(it.keys) }
                    .mapValues { it.value.toMediaItems() }
                    .toImmutableMap()

                reduce {
                    val selectedMangaList = if (state.currentMangaListName.isNullOrEmpty()) {
                        items.keys.firstOrNull()
                    } else {
                        state.currentMangaListName
                    }

                    val defaultList = items.defaultList(selectedMangaList)

                    state.copy(
                        currentMangaListName = selectedMangaList,
                        mangaCollection = items,
                        mangaListNames = mangaListNames.toImmutableList(),
                        currentMangaList = state.currentMangaList.copy(
                            items = defaultList,
                            isEmpty = defaultList.isEmpty(),
                            isLoading = false,
                        ),
                    )
                }
            }
        }
    }

    private fun <T : MediaListItem> Map<String, List<T>>.defaultList(
        defaultListName: String?,
    ) = getOrElse(defaultListName.orEmpty()) { emptyList() }.toImmutableList()
}
