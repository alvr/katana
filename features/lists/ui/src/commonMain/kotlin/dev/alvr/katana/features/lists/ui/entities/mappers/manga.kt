package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.core.common.orZero
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.ui.entities.MediaListItem

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
    startedAt = list.startedAt,
    completedAt = list.completedAt,
    updatedAt = list.updatedAt,
    volumesProgress = list.progressVolumes.orZero(),
    volumesTotal = entry.volumes,
)
