package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.ui.lists.entities.MediaListItem

internal fun List<MediaList<MediaEntry.Manga>>.toMediaItems() =
    flatMap { list -> list.entries.map(MediaListEntry<MediaEntry.Manga>::toMediaItem) }

private fun MediaListEntry<MediaEntry.Manga>.toMediaItem() = MediaListItem.MangaListItem(
    entryId = id,
    mediaId = media.id,
    title = media.title,
    score = score,
    format = media.format.toEntity(),
    cover = media.coverImage,
    progress = progress,
    total = media.chapters,
    updatedAt = updatedAt,
    volumesProgress = progressVolumes ?: 0,
    volumesTotal = media.volumes,
)
