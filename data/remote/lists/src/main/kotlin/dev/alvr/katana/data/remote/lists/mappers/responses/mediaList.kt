package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.common.core.orZero
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal fun <T : MediaEntry> MediaListCollectionQuery.Data.mediaList(type: MediaType): List<MediaListGroup<T>> =
    collection.lists.asSequence().map { list ->
        val entries = list?.entries.orEmpty().asSequence().mapNotNull { entry ->
            entry?.toModel<T>(type)
        }.toList()

        MediaListGroup(
            name = list?.name.orEmpty(),
            entries = entries,
        )
    }.sortedBy { sortLists(type, it.name) }.toList()

@Suppress("UNCHECKED_CAST")
private fun <T : MediaEntry> MediaListCollectionQuery.Entry.toModel(type: MediaType) =
    mediaListEntry.let { entry ->
        val list = MediaList(
            id = entry.id,
            score = entry.score.orZero(),
            progress = entry.progress.orZero(),
            progressVolumes = entry.progressVolumes,
            repeat = entry.repeat.orZero(),
            private = entry.private ?: false,
            notes = entry.notes.orEmpty(),
            hiddenFromStatusLists = entry.hiddenFromStatusLists ?: false,
            startedAt = entry.startedAt?.let { date ->
                dateMapper(date.day, date.month, date.year)
            },
            completedAt = entry.completedAt?.let { date ->
                dateMapper(date.day, date.month, date.year)
            },
            updatedAt = entry.updatedAt?.toLocalDateTime(),
        )
        MediaListEntry(
            list = list,
            entry = media.mediaEntry.toMedia(type) as T,
        )
    }

private fun MediaEntryFragment.toMedia(type: MediaType) = type.onMediaEntry(
    anime = ::animeEntry,
    manga = ::mangaEntry,
)

private fun MediaListCollectionQuery.Data.sortLists(type: MediaType, listName: String) =
    with(collection.user?.mediaListOptions) {
        type.onMediaEntry(
            anime = { this?.animeList?.sectionOrder.orEmpty().listPosition(listName) },
            manga = { this?.mangaList?.sectionOrder.orEmpty().listPosition(listName) },
        )
    }

private fun List<String?>.listPosition(listName: String) = if (listName.isEmpty()) {
    size
} else {
    indexOf(listName)
}
