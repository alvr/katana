package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal inline fun <reified T : MediaEntry> MediaListCollectionQuery.Data.mediaList(): List<MediaList<T>> =
    collection.lists.asSequence().map { list ->
        val entries = list?.entries.orEmpty().asSequence().mapNotNull { entry ->
            entry?.toModel<T>()
        }.toList()

        MediaList(
            name = list?.name.orEmpty(),
            listType = MediaList.ListType.of(list?.name),
            entries = entries,
        )
    }.sortedBy { it.listType }.toList()

private inline fun <reified T : MediaEntry> MediaListCollectionQuery.Entry.toModel() =
    mediaListEntry.let { entry ->
        MediaListEntry(
            id = entry.id,
            score = entry.score ?: 0.0,
            progress = entry.progress ?: 0,
            progressVolumes = entry.progressVolumes,
            repeat = entry.repeat ?: 0,
            priority = entry.priority ?: 0,
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
            media = media.mediaEntry.toMedia() as T
        )
    }

private inline fun <reified T : MediaEntry> MediaEntryFragment.toMedia(): T = when (T::class) {
    MediaEntry.Anime::class -> animeEntry()
    MediaEntry.Manga::class -> mangaEntry()
    else -> error("only MediaEntry.Anime and MediaEntry.Manga are accepted")
} as T
