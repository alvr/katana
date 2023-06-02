package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.TimezoneOffset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

@ExperimentalCoroutinesApi
internal class AnimeEntryMapperTest : TestBase() {
    @Test
    @DisplayName("WHEN a null entry THEN it should be mapped to the default values")
    fun `a null entry`() = runTest {
        // GIVEN
        val entry: MediaEntryFragment? = null

        // WHEN
        val result = entry.animeEntry()

        // THEN
        result shouldBeEqualToComparingFields MediaEntry.Anime(
            entry = CommonMediaEntry(
                id = Int.zero,
                title = String.empty,
                coverImage = String.empty,
                format = CommonMediaEntry.Format.UNKNOWN,
            ),
            episodes = null,
            nextEpisode = null,
        )
        result shouldBeEqualToComparingFields MediaEntry.Anime(
            entry = entry.mediaEntry(),
            episodes = null,
            nextEpisode = null,
        )
    }

    @Test
    @DisplayName("WHEN an entry with null values THEN it should be mapped to the default values")
    fun `an entry with null values`() = runTest {
        // GIVEN
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

        // WHEN
        val result = entry.animeEntry()

        // THEN
        result shouldBeEqualToComparingFields MediaEntry.Anime(
            entry = CommonMediaEntry(
                id = Int.zero,
                title = String.empty,
                coverImage = String.empty,
                format = CommonMediaEntry.Format.UNKNOWN,
            ),
            episodes = null,
            nextEpisode = null,
        )

        result shouldBeEqualToComparingFields MediaEntry.Anime(
            entry = entry.mediaEntry(),
            episodes = null,
            nextEpisode = null,
        )
    }

    @Test
    @DisplayName(
        """
        WHEN an entry with null values but data classes with null
        THEN it should be mapped to the default values
        """,
    )
    fun `an entry with null values but data classes with null`() = runTest {
        // GIVEN
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

        // WHEN
        val result = entry.animeEntry()

        // THEN
        result shouldBeEqualToComparingFields MediaEntry.Anime(
            entry = CommonMediaEntry(
                id = Int.zero,
                title = String.empty,
                coverImage = String.empty,
                format = CommonMediaEntry.Format.UNKNOWN,
            ),
            episodes = null,
            nextEpisode = null,
        )

        result shouldBeEqualToComparingFields MediaEntry.Anime(
            entry = entry.mediaEntry(),
            episodes = null,
            nextEpisode = null,
        )
    }

    @Test
    @DisplayName("WHEN an entry with all properties THEN it should be mapped to a `MediaEntry.Anime` class")
    fun `an entry with all properties`() = runTest {
        // GIVEN
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

        // WHEN
        val result = entry.animeEntry()

        // THEN
        result shouldBeEqualToComparingFields MediaEntry.Anime(
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

        result shouldBeEqualToComparingFields MediaEntry.Anime(
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
