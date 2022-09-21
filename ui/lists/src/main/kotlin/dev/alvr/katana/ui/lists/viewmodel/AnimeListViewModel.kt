package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems
import javax.inject.Inject

@HiltViewModel
internal class AnimeListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    updateListUseCase: UpdateListUseCase,
    private val observeAnimeListUseCase: ObserveAnimeListUseCase,
) : ListViewModel<MediaEntry.Anime, MediaListItem.AnimeListItem>(savedStateHandle, updateListUseCase) {
    override val collectionFlow get() = observeAnimeListUseCase.flow

    override fun List<MediaListGroup<MediaEntry.Anime>>.entryMap() = toMediaItems()

    override fun observeListUseCase() {
        observeAnimeListUseCase()
    }
}
