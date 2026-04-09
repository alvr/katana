package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.common.orZero
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.features.lists.data.MediaListCollectionQuery
import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment
import dev.alvr.katana.features.lists.domain.models.ItemEntryId
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.domain.models.lists.MediaListEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup

internal inline operator fun <reified T : MediaEntry> MediaListCollectionQuery.Data.invoke(
    type: MediaType
): List<MediaListGroup<T>> =
    mediaListCollection.lists
        .map { list ->
            MediaListGroup(name = list.name, entries = list.entries.map { entry -> entry.toModel<T>(type) }.toList())
        }
        .sortedBy { sortLists(type, it.name) }

private inline fun <reified T : MediaEntry> MediaListCollectionQuery.Entry.toModel(type: MediaType) =
    with(mediaListEntry) {
        MediaListEntry(
            list =
                MediaList(
                    id = ItemEntryId(id),
                    score = score,
                    progress = progress.orZero(),
                    progressVolumes = progressVolumes,
                    repeat = repeat.orZero(),
                    private = private ?: false,
                    notes = notes.orEmpty(),
                    hiddenFromStatusLists = hiddenFromStatusLists ?: false,
                    startedAt = startedAt?.let { date -> dateMapper(date.day, date.month, date.year) },
                    completedAt = completedAt?.let { date -> dateMapper(date.day, date.month, date.year) },
                    updatedAt = updatedAt?.toLocalDateTime(),
                ),
            entry = media.mediaEntry.toMedia(type) as T,
        )
    }

private fun MediaEntryFragment.toMedia(type: MediaType) = type.onMediaEntry(anime = ::animeEntry, manga = ::mangaEntry)

private fun MediaListCollectionQuery.Data.sortLists(type: MediaType, listName: String) =
    with(mediaListCollection.user.mediaListOptions) {
        type.onMediaEntry(
            anime = { animeList.sectionOrder.listPosition(listName) },
            manga = { mangaList.sectionOrder.listPosition(listName) },
        )
    }

private fun List<String?>.listPosition(listName: String) = indexOf(listName).takeIf { it >= Int.zero } ?: size
