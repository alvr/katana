package dev.alvr.katana.features.lists.ui.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaItems

@Stable
internal class AnimeListsViewModel(
    updateListUseCase: UpdateListUseCase,
    observeAnimeListUseCase: ObserveAnimeListUseCase,
) :
    ListsViewModel<MediaEntry.Anime, MediaListItem.AnimeListItem>(
        type = ListsState.ListType.Anime,
        updateListUseCase = updateListUseCase,
    ) {
    override val observeListUseCase = observeAnimeListUseCase

    override fun List<MediaListGroup<MediaEntry.Anime>>.entryMap() = toMediaItems()
}
