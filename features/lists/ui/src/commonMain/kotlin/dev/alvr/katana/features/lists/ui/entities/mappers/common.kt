package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.ui.entities.MediaListItem

internal fun CommonMediaEntry.Format.toEntity() = when (this) {
    CommonMediaEntry.Format.TV -> MediaListItem.Format.Tv
    CommonMediaEntry.Format.TV_SHORT -> MediaListItem.Format.TvShort
    CommonMediaEntry.Format.MOVIE -> MediaListItem.Format.Movie
    CommonMediaEntry.Format.SPECIAL -> MediaListItem.Format.Special
    CommonMediaEntry.Format.OVA -> MediaListItem.Format.Ova
    CommonMediaEntry.Format.ONA -> MediaListItem.Format.Ona
    CommonMediaEntry.Format.MUSIC -> MediaListItem.Format.Music
    CommonMediaEntry.Format.MANGA -> MediaListItem.Format.Manga
    CommonMediaEntry.Format.NOVEL -> MediaListItem.Format.Novel
    CommonMediaEntry.Format.ONE_SHOT -> MediaListItem.Format.OneShot
    CommonMediaEntry.Format.UNKNOWN -> MediaListItem.Format.Unknown
}

internal fun MediaListItem.toMediaList() = MediaList(
    id = entryId,
    score = score,
    progress = progress,
    progressVolumes = (this as? MediaListItem.MangaListItem)?.volumesProgress,
    repeat = repeat,
    private = private,
    notes = notes,
    hiddenFromStatusLists = hiddenFromStatusLists,
    startedAt = startedAt,
    completedAt = completedAt,
    updatedAt = updatedAt,
)
