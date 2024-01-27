package dev.alvr.katana.ui.lists.viewmodel

import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.item.MangaListItem
import dev.alvr.katana.ui.lists.entities.mappers.toMediaItems

internal class MangaListsViewModel(
    updateListUseCase: UpdateListUseCase,
    private val observeMangaListUseCase: ObserveMangaListUseCase,
) : ListsViewModel<MediaEntry.Manga, MangaListItem>(updateListUseCase) {
    override val collectionFlow get() = observeMangaListUseCase.flow

    override fun List<MediaListGroup<MediaEntry.Manga>>.entryMap() = toMediaItems()

    override fun observeListUseCase() {
        observeMangaListUseCase()
    }
}
