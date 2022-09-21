package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems
import javax.inject.Inject

@HiltViewModel
internal class MangaListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    updateListUseCase: UpdateListUseCase,
    private val observeMangaListUseCase: ObserveMangaListUseCase,
) : ListViewModel<MediaEntry.Manga, MediaListItem.MangaListItem>(savedStateHandle, updateListUseCase) {
    override val collectionFlow = observeMangaListUseCase.flow

    override fun List<MediaListGroup<MediaEntry.Manga>>.entryMap() = toMediaItems()

    override fun observeListUseCase() {
        observeMangaListUseCase()
    }
}
