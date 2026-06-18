package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.ui.entities.MediaListItem

internal fun MediaEntry.Anime.toMediaItem(list: MediaList) =
    with(list) {
        MediaListItem.AnimeListItem(
            entryId = id,
            mediaId = this@toMediaItem.id,
            title = title,
            score = score,
            format = format.toEntity(),
            cover = coverImage,
            progress = progress,
            total = episodes,
            repeat = repeat,
            private = private,
            notes = notes,
            hiddenFromStatusLists = hiddenFromStatusLists,
            startedAt = startedAt,
            completedAt = completedAt,
            updatedAt = updatedAt,
            nextEpisode = nextEpisode.nextEpisode(),
        )
    }

private fun MediaEntry.Anime.NextEpisode?.nextEpisode() =
    this?.let { next -> MediaListItem.AnimeListItem.NextEpisode(number = next.number, date = next.at) }
