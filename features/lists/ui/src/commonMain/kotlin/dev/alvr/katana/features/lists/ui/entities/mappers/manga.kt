package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.core.common.orZero
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.ui.entities.MediaListItem

/**
     * Create a MangaListItem by combining this manga entry with data from the provided media list.
     *
     * @param list The MediaList whose list-scoped fields (for example list id, status flags, and timestamps) are applied to the resulting item.
     * @return A MediaListItem.MangaListItem representing the merged entry and list data.
     */
    internal fun MediaEntry.Manga.toMediaItem(list: MediaList) =
    with(list) {
        MediaListItem.MangaListItem(
            entryId = id,
            mediaId = this@toMediaItem.id,
            title = title,
            score = score,
            format = format.toEntity(),
            cover = coverImage,
            progress = progress,
            total = chapters,
            repeat = repeat,
            private = private,
            notes = notes,
            hiddenFromStatusLists = hiddenFromStatusLists,
            startedAt = startedAt,
            completedAt = completedAt,
            updatedAt = updatedAt,
            volumesProgress = progressVolumes.orZero(),
            volumesTotal = volumes,
        )
    }
