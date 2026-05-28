package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.ui.entities.MediaListItem

/**
     * Creates a UI representation of an anime list entry by combining this anime entry with its parent list.
     *
     * @param list The parent [MediaList] whose per-list fields (entry id, score, progress, status timestamps, privacy, notes, etc.) are applied to the resulting item.
     * @return A [MediaListItem.AnimeListItem] containing fields sourced from both the anime entry and the given list.
     */
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

/**
     * Maps a nullable domain `MediaEntry.Anime.NextEpisode` into a UI `MediaListItem.AnimeListItem.NextEpisode`.
     *
     * @return A `MediaListItem.AnimeListItem.NextEpisode` containing the same episode number and date, or `null` if the receiver is `null`.
     */
    private fun MediaEntry.Anime.NextEpisode?.nextEpisode() =
    this?.let { next -> MediaListItem.AnimeListItem.NextEpisode(number = next.number, date = next.at) }
