package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.ui.lists.entities.MediaListItem

internal fun List<MediaListGroup<MediaEntry.Anime>>.toMediaItems() =
    flatMap { list -> list.entries.map(MediaListEntry<MediaEntry.Anime>::toMediaItem) }

private fun MediaList<MediaEntry.Anime>.toMediaItem() = MediaListItem.AnimeListItem(
    entryId = id,
    mediaId = media.id,
    title = media.title,
    score = score,
    format = media.format.toEntity(),
    cover = media.coverImage,
    progress = progress,
    total = media.episodes,
    updatedAt = updatedAt,
    nextEpisode = media.nextEpisode.nextEpisode(),
)

private fun MediaEntry.Anime.NextEpisode?.nextEpisode() = this?.let { next ->
    MediaListItem.AnimeListItem.NextEpisode(
        number = next.number,
        date = next.at,
    )
}
