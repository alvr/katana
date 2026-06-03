package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.core.common.orZero
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.ui.entities.MediaListItem

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
