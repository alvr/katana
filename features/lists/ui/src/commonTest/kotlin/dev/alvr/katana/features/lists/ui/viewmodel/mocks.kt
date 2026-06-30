package dev.alvr.katana.features.lists.ui.viewmodel

import dev.alvr.katana.common.media.domain.models.ItemEntryId
import dev.alvr.katana.common.media.domain.models.ItemMediaId
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.common.media.domain.models.lists.MediaListEntry
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

internal val animeListItem1 =
    MediaListItem.AnimeListItem(
        entryId = ItemEntryId(Int.zero),
        mediaId = ItemMediaId(Int.zero),
        title = "One Piece",
        score = Float.zero,
        format = MediaListItem.Format.OneShot,
        cover = String.empty,
        progress = 233,
        total = null,
        repeat = Int.zero,
        private = false,
        notes = String.empty,
        hiddenFromStatusLists = false,
        nextEpisode = null,
        startedAt = LocalDate(2020, 1, 20),
        completedAt = LocalDate(2023, 1, 20),
        updatedAt = LocalDateTime(LocalDate(2020, 1, 20), LocalTime(12, 0)),
    )

internal val animeListItem2 =
    MediaListItem.AnimeListItem(
        entryId = ItemEntryId(Int.zero),
        mediaId = ItemMediaId(Int.zero),
        title = String.empty,
        score = Float.zero,
        format = MediaListItem.Format.OneShot,
        cover = String.empty,
        progress = 234,
        total = null,
        repeat = Int.zero,
        private = true,
        notes = String.empty,
        hiddenFromStatusLists = false,
        nextEpisode =
            MediaListItem.AnimeListItem.NextEpisode(
                number = Int.zero,
                date = LocalDateTime(LocalDate(2020, 1, 20), LocalTime(12, 0)),
            ),
        startedAt = null,
        completedAt = null,
        updatedAt = null,
    )

internal val mangaListItem1 =
    MediaListItem.MangaListItem(
        entryId = ItemEntryId(Int.zero),
        mediaId = ItemMediaId(Int.zero),
        title = "One Piece",
        score = Float.zero,
        format = MediaListItem.Format.OneShot,
        cover = String.empty,
        progress = 233,
        total = Int.zero,
        volumesProgress = Int.zero,
        volumesTotal = Int.zero,
        repeat = Int.zero,
        private = false,
        notes = String.empty,
        hiddenFromStatusLists = false,
        startedAt = LocalDate(2020, 1, 20),
        completedAt = LocalDate(2023, 1, 20),
        updatedAt = LocalDateTime(LocalDate(2020, 1, 20), LocalTime(12, 0)),
    )

internal val mangaListItem2 =
    MediaListItem.MangaListItem(
        entryId = ItemEntryId(Int.zero),
        mediaId = ItemMediaId(Int.zero),
        title = String.empty,
        score = Float.zero,
        format = MediaListItem.Format.OneShot,
        cover = String.empty,
        progress = 234,
        volumesProgress = Int.zero,
        total = null,
        volumesTotal = null,
        repeat = Int.zero,
        private = true,
        notes = String.empty,
        hiddenFromStatusLists = false,
        startedAt = null,
        completedAt = null,
        updatedAt = null,
    )

internal val animeMediaEntry1 =
    MediaListEntry(
        list =
            MediaList(
                id = ItemEntryId(Int.zero),
                score = Float.zero,
                progress = 233,
                progressVolumes = Int.zero,
                repeat = Int.zero,
                private = false,
                notes = String.empty,
                hiddenFromStatusLists = false,
                startedAt = LocalDate(2020, 1, 20),
                completedAt = LocalDate(2023, 1, 20),
                updatedAt = LocalDateTime(LocalDate(2020, 1, 20), LocalTime(12, 0)),
            ),
        entry =
            MediaEntry.Anime(
                entry =
                    CommonMediaEntry(
                        id = ItemMediaId(Int.zero),
                        title = "One Piece",
                        coverImage = String.empty,
                        format = CommonMediaEntry.Format.ONE_SHOT,
                    ),
                episodes = null,
                nextEpisode = null,
            ),
    )

internal val animeMediaEntry2 =
    MediaListEntry(
        list =
            MediaList(
                id = ItemEntryId(Int.zero),
                score = Float.zero,
                progress = 234,
                progressVolumes = Int.zero,
                repeat = Int.zero,
                private = true,
                notes = String.empty,
                hiddenFromStatusLists = false,
                startedAt = null,
                completedAt = null,
                updatedAt = null,
            ),
        entry =
            MediaEntry.Anime(
                entry =
                    CommonMediaEntry(
                        id = ItemMediaId(Int.zero),
                        title = String.empty,
                        coverImage = String.empty,
                        format = CommonMediaEntry.Format.ONE_SHOT,
                    ),
                episodes = null,
                nextEpisode =
                    MediaEntry.Anime.NextEpisode(
                        number = Int.zero,
                        at = LocalDateTime(LocalDate(2020, 1, 20), LocalTime(12, 0)),
                    ),
            ),
    )

internal val mangaMediaEntry1 =
    MediaListEntry(
        list =
            MediaList(
                id = ItemEntryId(Int.zero),
                score = Float.zero,
                progress = 233,
                progressVolumes = Int.zero,
                repeat = Int.zero,
                private = false,
                notes = String.empty,
                hiddenFromStatusLists = false,
                startedAt = LocalDate(2020, 1, 20),
                completedAt = LocalDate(2023, 1, 20),
                updatedAt = LocalDateTime(LocalDate(2020, 1, 20), LocalTime(12, 0)),
            ),
        entry =
            MediaEntry.Manga(
                entry =
                    CommonMediaEntry(
                        id = ItemMediaId(Int.zero),
                        title = "One Piece",
                        coverImage = String.empty,
                        format = CommonMediaEntry.Format.ONE_SHOT,
                    ),
                chapters = Int.zero,
                volumes = Int.zero,
            ),
    )

internal val mangaMediaEntry2 =
    MediaListEntry(
        list =
            MediaList(
                id = ItemEntryId(Int.zero),
                score = Float.zero,
                progress = 234,
                progressVolumes = Int.zero,
                repeat = Int.zero,
                private = true,
                notes = String.empty,
                hiddenFromStatusLists = false,
                startedAt = null,
                completedAt = null,
                updatedAt = null,
            ),
        entry =
            MediaEntry.Manga(
                entry =
                    CommonMediaEntry(
                        id = ItemMediaId(Int.zero),
                        title = String.empty,
                        coverImage = String.empty,
                        format = CommonMediaEntry.Format.ONE_SHOT,
                    ),
                chapters = null,
                volumes = null,
            ),
    )
