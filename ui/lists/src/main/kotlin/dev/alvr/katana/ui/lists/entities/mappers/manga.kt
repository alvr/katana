package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.common.core.orZero
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.ui.lists.entities.MediaListItem

internal fun List<MediaListGroup<MediaEntry.Manga>>.toMediaItems() =
    flatMap { list -> list.entries.map(MediaListEntry<MediaEntry.Manga>::toMediaItem) }

private fun MediaListEntry<MediaEntry.Manga>.toMediaItem() = MediaListItem.MangaListItem(
    entryId = list.id,
    mediaId = entry.id,
    title = entry.title,
    score = list.score,
    format = entry.format.toEntity(),
    cover = entry.coverImage,
    progress = list.progress,
    total = entry.chapters,
    repeat = list.repeat,
    private = list.private,
    notes = list.notes,
    hiddenFromStatusLists = list.hiddenFromStatusLists,
    startDate = entry.startDate,
    endDate = entry.endDate,
    startedAt = list.startedAt,
    completedAt = list.completedAt,
    updatedAt = list.updatedAt,
    volumesProgress = list.progressVolumes.orZero(),
    volumesTotal = entry.volumes,
)
