package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.MediaList
import dev.alvr.katana.domain.lists.models.MediaListEntry
import dev.alvr.katana.domain.lists.models.entries.AnimeEntry
import dev.alvr.katana.domain.lists.models.entries.MangaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import kotlin.time.ExperimentalTime
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

@OptIn(ExperimentalTime::class)
internal inline fun <reified T : MediaEntry> MediaListCollectionQuery.Data?.mediaList(): MediaCollection<T> {
    val lists = this?.collection?.lists?.asSequence().orEmpty().mapNotNull { list ->
        val entries = list?.entries?.asSequence().orEmpty().mapNotNull { entry ->
            entry.toModel<T>()
        }

        MediaList(
            name = list?.name.orEmpty(),
            listType = MediaList.ListType.of(list?.name),
            entries = entries.toList(),
        )
    }.sortedBy { it.listType }

    return MediaCollection(lists.toList())
}

private inline fun <reified T : MediaEntry> MediaListCollectionQuery.Entry?.toModel() =
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
            media = this?.media?.mediaEntry.toMedia<T>()
        )
    }

private inline fun <reified T : MediaEntry> MediaEntryFragment?.toMedia(): T = when (T::class) {
    AnimeEntry::class -> this?.animeEntry()
    MangaEntry::class -> this?.mangaEntry()
    else -> error("only AnimeEntry and MangaEntry are accepted")
} as T
