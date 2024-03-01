package dev.alvr.katana.features.lists.ui.viewmodel

import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaItems

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
