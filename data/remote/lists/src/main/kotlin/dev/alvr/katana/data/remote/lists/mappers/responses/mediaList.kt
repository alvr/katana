package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

@Suppress("UNCHECKED_CAST")
internal fun <T : MediaEntry> MediaListCollectionQuery.Data?.mediaList(type: MediaType): MediaCollection<T> {
    val lists = this?.collection?.lists?.asSequence().orEmpty().mapNotNull { list ->
        val entries = list?.entries?.asSequence().orEmpty().mapNotNull { entry ->
            entry.toModel(type)
        }

        MediaList(
            name = list?.name.orEmpty(),
            listType = MediaList.ListType.of(list?.name),
            entries = entries.toList(),
        )
    }.sortedBy { it.listType }

    return MediaCollection(lists.toList() as List<MediaList<T>>)
}

private fun MediaListCollectionQuery.Entry?.toModel(type: MediaType) =
    this?.mediaListEntry.let { entry ->
        MediaListEntry(
            id = entry?.id ?: 0,
            score = entry?.score ?: 0.0,
            progress = entry?.progress ?: 0,
            progressVolumes = entry?.progressVolumes,
            repeat = entry?.repeat ?: 0,
            priority = entry?.priority ?: 0,
            private = entry?.private ?: false,
            notes = entry?.notes.orEmpty(),
            hiddenFromStatusLists = entry?.hiddenFromStatusLists ?: false,
            startedAt = entry?.startedAt?.let { date ->
                dateMapper(date.day, date.month, date.year)
            },
            completedAt = entry?.completedAt?.let { date ->
                dateMapper(date.day, date.month, date.year)
            },
            updatedAt = entry?.updatedAt?.toLocalDateTime(),
            media = this?.media?.mediaEntry.toMedia(type)
        )
    }

private fun MediaEntryFragment?.toMedia(type: MediaType): MediaEntry = when (type) {
    MediaType.ANIME -> this?.animeEntry()
    MediaType.MANGA -> this?.mangaEntry()
    MediaType.UNKNOWN__ -> error("only AnimeEntry and MangaEntry are accepted")
} as MediaEntry
