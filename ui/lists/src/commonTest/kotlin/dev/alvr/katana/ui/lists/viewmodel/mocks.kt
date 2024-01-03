package dev.alvr.katana.ui.lists.viewmodel

import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.ui.lists.entities.MediaListItem
import korlibs.time.Date
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.TimezoneOffset

internal val animeListItem1 = MediaListItem.AnimeListItem(
    entryId = Int.zero,
    mediaId = Int.zero,
    title = "One Piece",
    score = Double.zero,
    format = MediaListItem.Format.OneShot,
    cover = String.empty,
    progress = 233,
    total = null,
    repeat = Int.zero,
    private = false,
    notes = String.empty,
    hiddenFromStatusLists = false,
    nextEpisode = null,
    startedAt = Date(Int.MAX_VALUE),
    completedAt = Date(Int.MAX_VALUE),
    updatedAt = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
)

internal val animeListItem2 = MediaListItem.AnimeListItem(
    entryId = Int.zero,
    mediaId = Int.zero,
    title = String.empty,
    score = Double.zero,
    format = MediaListItem.Format.OneShot,
    cover = String.empty,
    progress = 234,
    total = null,
    repeat = Int.zero,
    private = true,
    notes = String.empty,
    hiddenFromStatusLists = false,
    nextEpisode = MediaListItem.AnimeListItem.NextEpisode(
        number = Int.zero,
        date = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
    ),
    startedAt = Date(Int.MAX_VALUE),
    completedAt = Date(Int.MAX_VALUE),
    updatedAt = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
)

internal val mangaListItem1 = MediaListItem.MangaListItem(
    entryId = Int.zero,
    mediaId = Int.zero,
    title = "One Piece",
    score = Double.zero,
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
    startedAt = Date(Int.MAX_VALUE),
    completedAt = Date(Int.MAX_VALUE),
    updatedAt = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
)

internal val mangaListItem2 = MediaListItem.MangaListItem(
    entryId = Int.zero,
    mediaId = Int.zero,
    title = String.empty,
    score = Double.zero,
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
    startedAt = Date(Int.MAX_VALUE),
    completedAt = Date(Int.MAX_VALUE),
    updatedAt = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
)

internal val animeMediaEntry1 = MediaListEntry(
    list = MediaList(
        id = Int.zero,
        score = Double.zero,
        progress = 233,
        progressVolumes = Int.zero,
        repeat = Int.zero,
        private = false,
        notes = String.empty,
        hiddenFromStatusLists = false,
        startedAt = Date(Int.MAX_VALUE),
        completedAt = Date(Int.MAX_VALUE),
        updatedAt = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
    ),
    entry = MediaEntry.Anime(
        entry = CommonMediaEntry(
            id = Int.zero,
            title = "One Piece",
            coverImage = String.empty,
            format = CommonMediaEntry.Format.ONE_SHOT,
        ),
        episodes = null,
        nextEpisode = null,
    ),
)

internal val animeMediaEntry2 = MediaListEntry(
    list = MediaList(
        id = Int.zero,
        score = Double.zero,
        progress = 234,
        progressVolumes = Int.zero,
        repeat = Int.zero,
        private = true,
        notes = String.empty,
        hiddenFromStatusLists = false,
        startedAt = Date(Int.MAX_VALUE),
        completedAt = Date(Int.MAX_VALUE),
        updatedAt = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
    ),
    entry = MediaEntry.Anime(
        entry = CommonMediaEntry(
            id = Int.zero,
            title = String.empty,
            coverImage = String.empty,
            format = CommonMediaEntry.Format.ONE_SHOT,
        ),
        episodes = null,
        nextEpisode = MediaEntry.Anime.NextEpisode(
            number = Int.zero,
            at = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
        ),
    ),
)

internal val mangaMediaEntry1 = MediaListEntry(
    list = MediaList(
        id = Int.zero,
        score = Double.zero,
        progress = 233,
        progressVolumes = Int.zero,
        repeat = Int.zero,
        private = false,
        notes = String.empty,
        hiddenFromStatusLists = false,
        startedAt = Date(Int.MAX_VALUE),
        completedAt = Date(Int.MAX_VALUE),
        updatedAt = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
    ),
    entry = MediaEntry.Manga(
        entry = CommonMediaEntry(
            id = Int.zero,
            title = "One Piece",
            coverImage = String.empty,
            format = CommonMediaEntry.Format.ONE_SHOT,
        ),
        chapters = Int.zero,
        volumes = Int.zero,
    ),
)

internal val mangaMediaEntry2 = MediaListEntry(
    list = MediaList(
        id = Int.zero,
        score = Double.zero,
        progress = 234,
        progressVolumes = Int.zero,
        repeat = Int.zero,
        private = true,
        notes = String.empty,
        hiddenFromStatusLists = false,
        startedAt = Date(Int.MAX_VALUE),
        completedAt = Date(Int.MAX_VALUE),
        updatedAt = DateTimeTz.local(DateTime(Long.MAX_VALUE), TimezoneOffset.UTC),
    ),
    entry = MediaEntry.Manga(
        entry = CommonMediaEntry(
            id = Int.zero,
            title = String.empty,
            coverImage = String.empty,
            format = CommonMediaEntry.Format.ONE_SHOT,
        ),
        chapters = null,
        volumes = null,
    ),
)
