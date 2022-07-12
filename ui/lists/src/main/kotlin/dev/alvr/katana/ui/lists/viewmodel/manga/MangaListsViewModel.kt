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

    private fun observeMangaLists() {
        observeMangaListUseCase()

        intent {
            observeMangaListUseCase.flow.collect { active ->
                reduce { state.copy(currentListItems = active.lists.toMediaItems()) }
            }
        }
    }
}
