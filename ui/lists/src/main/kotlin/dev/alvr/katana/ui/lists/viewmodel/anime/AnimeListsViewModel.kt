package dev.alvr.katana.ui.lists.viewmodel.anime

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems
import dev.alvr.katana.ui.lists.viewmodel.ListsBaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class AnimeListsViewModel @Inject constructor(
    updateListUseCase: UpdateListUseCase,
    private val observeAnimeListUseCase: ObserveAnimeListUseCase,
) : ListsBaseViewModel<MediaEntry.Anime, MediaListItem.AnimeListItem>(updateListUseCase) {
    override val listsFlow get() = observeAnimeListUseCase.flow

    override fun fetchLists() {
        super.fetchLists()
        observeAnimeListUseCase()
    }

    override fun List<MediaListGroup<MediaEntry.Anime>>.listMapper() = toMediaItems()
}
