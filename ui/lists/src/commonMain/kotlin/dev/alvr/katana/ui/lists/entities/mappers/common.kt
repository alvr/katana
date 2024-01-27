package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.ui.lists.entities.item.Manga
import dev.alvr.katana.ui.lists.entities.item.MangaListItem
import dev.alvr.katana.ui.lists.entities.item.MediaListItem
import dev.alvr.katana.ui.lists.entities.item.Movie
import dev.alvr.katana.ui.lists.entities.item.Music
import dev.alvr.katana.ui.lists.entities.item.Novel
import dev.alvr.katana.ui.lists.entities.item.Ona
import dev.alvr.katana.ui.lists.entities.item.OneShot
import dev.alvr.katana.ui.lists.entities.item.Ova
import dev.alvr.katana.ui.lists.entities.item.Special
import dev.alvr.katana.ui.lists.entities.item.Tv
import dev.alvr.katana.ui.lists.entities.item.TvShort
import dev.alvr.katana.ui.lists.entities.item.Unknown

internal fun CommonMediaEntry.Format.toEntity() = when (this) {
    CommonMediaEntry.Format.TV -> Tv
    CommonMediaEntry.Format.TV_SHORT -> TvShort
    CommonMediaEntry.Format.MOVIE -> Movie
    CommonMediaEntry.Format.SPECIAL -> Special
    CommonMediaEntry.Format.OVA -> Ova
    CommonMediaEntry.Format.ONA -> Ona
    CommonMediaEntry.Format.MUSIC -> Music
    CommonMediaEntry.Format.MANGA -> Manga
    CommonMediaEntry.Format.NOVEL -> Novel
    CommonMediaEntry.Format.ONE_SHOT -> OneShot
    CommonMediaEntry.Format.UNKNOWN -> Unknown
}

internal fun MediaListItem.toMediaList() = MediaList(
    id = entryId,
    score = score,
    progress = progress,
    progressVolumes = (this as? MangaListItem)?.volumesProgress,
    repeat = repeat,
    private = private,
    notes = notes,
    hiddenFromStatusLists = hiddenFromStatusLists,
    startedAt = startedAt,
    completedAt = completedAt,
    updatedAt = updatedAt,
)
