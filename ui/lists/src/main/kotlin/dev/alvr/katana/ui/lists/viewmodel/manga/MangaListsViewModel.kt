package dev.alvr.katana.ui.lists.viewmodel.manga

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems
import dev.alvr.katana.ui.lists.viewmodel.ListsBaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class MangaListsViewModel @Inject constructor(
    updateListUseCase: UpdateListUseCase,
    private val observeMangaListUseCase: ObserveMangaListUseCase,
) : ListsBaseViewModel<MediaEntry.Manga, MediaListItem.MangaListItem>(updateListUseCase) {
    override val listsFlow = observeMangaListUseCase.flow

    override fun fetchLists() {
        super.fetchLists()
        observeMangaListUseCase()
    }

    override fun List<MediaListGroup<MediaEntry.Manga>>.listMapper() = toMediaItems()
}
