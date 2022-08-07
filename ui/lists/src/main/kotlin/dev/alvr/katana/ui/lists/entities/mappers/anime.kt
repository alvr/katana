package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.ui.lists.entities.MediaListItem
import kotlinx.collections.immutable.toImmutableList

internal fun List<MediaListGroup<MediaEntry.Anime>>.toMediaItems() =
    flatMap { list -> list.entries.map(MediaListEntry<MediaEntry.Anime>::toMediaItem) }
        .toImmutableList()

private fun MediaListEntry<MediaEntry.Anime>.toMediaItem() = MediaListItem.AnimeListItem(
    entryId = list.id,
    mediaId = entry.id,
    title = entry.title,
    score = list.score,
    format = entry.format.toEntity(),
    cover = entry.coverImage,
    progress = list.progress,
    total = entry.episodes,
    repeat = list.repeat,
    private = list.private,
    notes = list.notes,
    hiddenFromStatusLists = list.hiddenFromStatusLists,
    startedAt = list.startedAt,
    completedAt = list.completedAt,
    updatedAt = list.updatedAt,
    nextEpisode = entry.nextEpisode.nextEpisode(),
)

private fun MediaEntry.Anime.NextEpisode?.nextEpisode() = this?.let { next ->
    MediaListItem.AnimeListItem.NextEpisode(
        number = next.number,
        date = next.at,
    )
}
