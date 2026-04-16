package dev.alvr.katana.features.lists.ui.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.usecases.ObserveMangaListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaItems
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Stable
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class MangaListsViewModel(
    dispatcher: KatanaDispatcher,
    updateListUseCase: UpdateListUseCase,
    observeMangaListUseCase: ObserveMangaListUseCase,
) :
    ListsViewModel<MediaEntry.Manga, MediaListItem.MangaListItem>(
        dispatcher = dispatcher,
        type = ListsState.ListType.Manga,
        updateListUseCase = updateListUseCase,
    ) {
    override val observeListUseCase = observeMangaListUseCase

    override fun List<MediaListGroup<MediaEntry.Manga>>.entryMap() = toMediaItems()
}
