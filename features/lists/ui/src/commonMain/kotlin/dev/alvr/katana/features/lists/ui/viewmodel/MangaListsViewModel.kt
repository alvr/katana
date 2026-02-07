package dev.alvr.katana.features.lists.ui.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.usecases.ObserveMangaListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaItems

@Stable
internal class MangaListsViewModel(
    updateListUseCase: UpdateListUseCase,
    observeMangaListUseCase: ObserveMangaListUseCase,
) :
    ListsViewModel<MediaEntry.Manga, MediaListItem.MangaListItem>(
        type = ListsState.ListType.Manga,
        updateListUseCase = updateListUseCase,
    ) {
    override val observeListUseCase = observeMangaListUseCase

    override fun List<MediaListGroup<MediaEntry.Manga>>.entryMap() = toMediaItems()
}
