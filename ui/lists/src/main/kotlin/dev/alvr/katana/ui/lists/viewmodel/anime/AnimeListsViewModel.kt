package dev.alvr.katana.ui.lists.viewmodel.anime

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.ui.base.viewmodel.BaseViewModel
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems
import javax.inject.Inject
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class AnimeListsViewModel @Inject constructor(
    private val observeAnimeListUseCase: ObserveAnimeListUseCase,
) : BaseViewModel<AnimeListsState, Nothing>() {
    override val container = container<AnimeListsState, Nothing>(AnimeListsState()) {
        observeAnimeLists()
    }

    fun fetchAnimeLists() {
        updateState { copy(isLoading = true) }
        observeAnimeListUseCase()
    }

    private fun observeAnimeLists() {
        fetchAnimeLists()

        intent {
            observeAnimeListUseCase.flow.collect { active ->
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
