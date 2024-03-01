package dev.alvr.katana.features.lists.ui.viewmodel

import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.usecases.ObserveMangaListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaItems

internal class MangaListsViewModel(
    updateListUseCase: UpdateListUseCase,
    private val observeMangaListUseCase: ObserveMangaListUseCase,
) : ListsViewModel<MediaEntry.Manga, MediaListItem.MangaListItem>(updateListUseCase) {
    override val collectionFlow get() = observeMangaListUseCase.flow

    override fun List<MediaListGroup<MediaEntry.Manga>>.entryMap() = toMediaItems()

    override fun observeListUseCase() {
        observeMangaListUseCase()
    }
}
