package dev.alvr.katana.ui.lists.viewmodel.manga

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems
import javax.inject.Inject
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class MangaListsViewModel @Inject constructor(
    private val observeMangaListUseCase: ObserveMangaListUseCase,
) : BaseViewModel<MangaListsState, Nothing>() {
    override val container = container<MangaListsState, Nothing>(MangaListsState()) {
        observeMangaLists()
    }

    fun fetchMangaLists() {
        updateState { copy(isLoading = true) }
        observeMangaListUseCase()
    }

    private fun observeMangaLists() {
        fetchMangaLists()

        intent {
            observeMangaListUseCase.flow.collect { active ->
                val items = active.lists.toMediaItems()

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
