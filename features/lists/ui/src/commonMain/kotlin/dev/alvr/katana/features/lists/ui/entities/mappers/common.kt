package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.domain.models.lists.MediaListEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import kotlinx.collections.immutable.toImmutableMap

/**
         * Builds an immutable map that keys each media list item's `entryId` to its corresponding `MediaListItem`.
         *
         * @return An immutable `Map` from `entryId` to `MediaListItem` containing one entry per `MediaListEntry` found in the input groups.
         */
        internal fun Iterable<MediaListGroup<MediaEntry>>.entryMap() =
    flatMap { group -> group.entries.map(MediaListEntry<out MediaEntry>::toMediaItem) }
        .associateBy { item -> item.entryId }
        .toImmutableMap()

/**
     * Converts this MediaListEntry containing a MediaEntry into the corresponding UI MediaListItem.
     *
     * @receiver The MediaListEntry to convert; its `list` context is used when mapping the underlying entry.
     * @return The MediaListItem representing the underlying `MediaEntry.Anime` or `MediaEntry.Manga`.
     */
    private fun MediaListEntry<MediaEntry>.toMediaItem() =
    when (val media = entry) {
        is MediaEntry.Anime -> media.toMediaItem(list)
        is MediaEntry.Manga -> media.toMediaItem(list)
    }

/**
     * Maps a CommonMediaEntry.Format value to the corresponding MediaListItem.Format.
     *
     * @return The corresponding MediaListItem.Format value.
     */
    internal fun CommonMediaEntry.Format.toEntity() =
    when (this) {
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

internal fun MediaListItem.toMediaList() =
    MediaList(
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
