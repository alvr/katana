package dev.alvr.katana.ui.lists.viewmodel

import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems

internal class AnimeListsViewModel(
    updateListUseCase: UpdateListUseCase,
    private val observeAnimeListUseCase: ObserveAnimeListUseCase,
) : ListsViewModel<MediaEntry.Anime, MediaListItem.AnimeListItem>(updateListUseCase) {
    override val collectionFlow get() = observeAnimeListUseCase.flow

    override fun List<MediaListGroup<MediaEntry.Anime>>.entryMap() = toMediaItems()

    override fun observeListUseCase() {
        observeAnimeListUseCase()
    }
}
