package dev.alvr.katana.data.remote.lists.mappers

import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.data.remote.lists.mappers.responses.animeEntry
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaEntry
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import java.time.LocalDateTime
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal class AnimeEntryMapperTest : WordSpec({
    "a null entry" should {
        val entry: MediaEntryFragment? = null

        "be mapped to the default values" {
            entry.animeEntry() shouldBeEqualToComparingFields MediaEntry.Anime(
                entry = CommonMediaEntry(
                    id = 0,
                    title = "",
                    coverImage = "",
                    format = CommonMediaEntry.Format.UNKNOWN,
                    genres = emptyList(),
                ),
                episodes = null,
                nextEpisode = null,
            )

            entry.animeEntry() shouldBeEqualToComparingFields MediaEntry.Anime(
                entry = entry.mediaEntry(),
                episodes = null,
                nextEpisode = null,
            )
        }
    }

    "an entry with null values" should {
        val entry = MediaEntryFragment(
            id = 0,
            title = null,
            episodes = null,
            duration = null,
            chapters = null,
            volumes = null,
            format = null,
            coverImage = null,
            genres = null,
            nextAiringEpisode = null
        )

        "be mapped to the default values" {
            entry.animeEntry() shouldBeEqualToComparingFields MediaEntry.Anime(
                entry = CommonMediaEntry(
                    id = 0,
                    title = "",
                    coverImage = "",
                    format = CommonMediaEntry.Format.UNKNOWN,
                    genres = emptyList(),
                ),
                episodes = null,
                nextEpisode = null,
            )

            entry.animeEntry() shouldBeEqualToComparingFields MediaEntry.Anime(
                entry = entry.mediaEntry(),
                episodes = null,
                nextEpisode = null,
            )
        }
    }

    "an entry with null values but data classes with null" should {
        val entry = MediaEntryFragment(
            id = 0,
            title = MediaEntryFragment.Title(null),
            episodes = null,
            duration = null,
            chapters = null,
            volumes = null,
            format = null,
            coverImage = MediaEntryFragment.CoverImage(null),
            genres = null,
            nextAiringEpisode = null,
        )

        "be mapped to the default values" {
            entry.animeEntry() shouldBeEqualToComparingFields MediaEntry.Anime(
                entry = CommonMediaEntry(
                    id = 0,
                    title = "",
                    coverImage = "",
                    format = CommonMediaEntry.Format.UNKNOWN,
                    genres = emptyList(),
                ),
                episodes = null,
                nextEpisode = null,
            )

            entry.animeEntry() shouldBeEqualToComparingFields MediaEntry.Anime(
                entry = entry.mediaEntry(),
                episodes = null,
                nextEpisode = null,
            )
        }
    }

    "an entry with all properties" should {
        val entry = MediaEntryFragment(
            id = 0,
            title = MediaEntryFragment.Title("One Piece"),
            episodes = 1000,
            duration = 23,
            chapters = null,
            volumes = null,
            format = MediaFormat.ONA,
            coverImage = MediaEntryFragment.CoverImage("https://www.fillmurray.com/128/256"),
            genres = listOf("Comedy", "Slice of Life"),
            nextAiringEpisode = MediaEntryFragment.NextAiringEpisode(1_241_517_600, 1001),
        )

        "be mapped to a `MediaEntry.Anime` class" {
            entry.animeEntry() shouldBeEqualToComparingFields MediaEntry.Anime(
                entry = CommonMediaEntry(
                    id = 0,
                    title = "One Piece",
                    coverImage = "https://www.fillmurray.com/128/256",
                    format = CommonMediaEntry.Format.ONA,
                    genres = listOf("Comedy", "Slice of Life"),
                ),
                episodes = 1000,
                nextEpisode = MediaEntry.Anime.NextEpisode(
                    1001,
                    LocalDateTime.of(2009, 5, 5, 12, 0, 0)
                ),
            )

            entry.animeEntry() shouldBeEqualToComparingFields MediaEntry.Anime(
                entry = entry.mediaEntry(),
                episodes = 1000,
                nextEpisode = MediaEntry.Anime.NextEpisode(
                    1001,
                    LocalDateTime.of(2009, 5, 5, 12, 0, 0)
                ),
            )
        }
    }
})
