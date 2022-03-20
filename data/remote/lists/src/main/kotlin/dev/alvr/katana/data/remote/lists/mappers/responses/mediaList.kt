package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.domain.lists.models.AnimeEntry
import dev.alvr.katana.domain.lists.models.MangaEntry
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.MediaEntry
import dev.alvr.katana.domain.lists.models.MediaList
import dev.alvr.katana.domain.lists.models.MediaStatus

internal inline fun <reified T : MediaEntry> MediaListCollectionQuery.Data?.mediaList(): List<MediaCollection<T>> =
    this?.collection?.lists?.mapNotNull { list ->
        MediaCollection(
            entries = list?.entries?.mapNotNull { entry ->
                MediaList(
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
                    media = entry?.media?.let { media ->
                        when (T::class) {
                            AnimeEntry::class -> media.animeEntry()
                            MangaEntry::class -> media.mangaEntry()
                            else -> error("only AnimeEntry and MangaEntry are accepted")
                        }
                    } as T
                )
            }.orEmpty(),
            name = list?.name.orEmpty(),
            isCustomList = list?.isCustomList ?: false,
            isSplitCompletedList = list?.isSplitCompletedList ?: false,
        )
    }.orEmpty()
