package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.TimezoneOffset
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal class AnimeEntryMapperTest : FreeSpec({
    "a null entry" {
        val entry: MediaEntryFragment? = null
        entry.animeEntry().also { result ->
            result shouldBeEqual MediaEntry.Anime(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                episodes = null,
                nextEpisode = null,
            )
            result shouldBeEqual MediaEntry.Anime(
                entry = entry.mediaEntry(),
                episodes = null,
                nextEpisode = null,
            )
        }
    }

    "an entry with null values" {
        val entry = MediaEntryFragment(
            id = Int.zero,
            title = null,
            episodes = null,
            chapters = null,
            volumes = null,
            format = null,
            coverImage = null,
            nextAiringEpisode = null,
        )
        entry.animeEntry().also { result ->
            result shouldBeEqual MediaEntry.Anime(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                episodes = null,
                nextEpisode = null,
            )
            result shouldBeEqual MediaEntry.Anime(
                entry = entry.mediaEntry(),
                episodes = null,
                nextEpisode = null,
            )
        }
    }

    "an entry with null values but data classes with null" {
        val entry = MediaEntryFragment(
            id = Int.zero,
            title = MediaEntryFragment.Title(null),
            episodes = null,
            chapters = null,
            volumes = null,
            format = null,
            coverImage = MediaEntryFragment.CoverImage(null),
            nextAiringEpisode = null,
        )

        entry.animeEntry().also { result ->
            result shouldBeEqual MediaEntry.Anime(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                episodes = null,
                nextEpisode = null,
            )
            result shouldBeEqual MediaEntry.Anime(
                entry = entry.mediaEntry(),
                episodes = null,
                nextEpisode = null,
            )
        }
    }

    "an entry with all properties" {
        val entry = MediaEntryFragment(
            id = Int.zero,
            title = MediaEntryFragment.Title("One Piece"),
            episodes = 1000,
            chapters = null,
            volumes = null,
            format = MediaFormat.ONA,
            coverImage = MediaEntryFragment.CoverImage("https://placehold.co/128x256"),
            nextAiringEpisode = MediaEntryFragment.NextAiringEpisode(1_241_517_600, 1001),
        )

        entry.animeEntry().also { result ->
            result shouldBeEqual MediaEntry.Anime(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = "One Piece",
                    coverImage = "https://placehold.co/128x256",
                    format = CommonMediaEntry.Format.ONA,
                ),
                episodes = 1000,
                nextEpisode = MediaEntry.Anime.NextEpisode(
                    1001,
                    DateTimeTz.local(
                        DateTime(2009, 5, 5, 10, 0, 0),
                        TimezoneOffset.UTC,
                    ),
                ),
            )
            result shouldBeEqual MediaEntry.Anime(
                entry = entry.mediaEntry(),
                episodes = 1000,
                nextEpisode = MediaEntry.Anime.NextEpisode(
                    1001,
                    DateTimeTz.local(
                        DateTime(2009, 5, 5, 10, 0, 0),
                        TimezoneOffset.UTC,
                    ),
                ),
            )
        }
    }
})
