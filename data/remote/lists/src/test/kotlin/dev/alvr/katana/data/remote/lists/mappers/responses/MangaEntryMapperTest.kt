package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry.EndDate
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry.StartDate
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

@ExperimentalCoroutinesApi
internal class MangaEntryMapperTest : TestBase() {
    @Test
    @DisplayName("WHEN a null entry THEN it should be mapped to the default values")
    fun `a null entry`() = runTest {
        // GIVEN
        val entry: MediaEntryFragment? = null

        // WHEN
        val result = entry.mangaEntry()

        // THEN
        result shouldBeEqualToComparingFields MediaEntry.Manga(
            entry = CommonMediaEntry(
                id = Int.zero,
                title = String.empty,
                coverImage = String.empty,
                format = CommonMediaEntry.Format.UNKNOWN,
                startDate = LocalDate.now(),
                endDate = null,
            ),
            chapters = null,
            volumes = null,
        )

        result shouldBeEqualToComparingFields MediaEntry.Manga(
            entry = entry.mediaEntry(),
            chapters = null,
            volumes = null,
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
            startDate = null,
            endDate = null,
        )

        // WHEN
        val result = entry.mangaEntry()

        // THEN
        result shouldBeEqualToComparingFields MediaEntry.Manga(
            entry = CommonMediaEntry(
                id = Int.zero,
                title = String.empty,
                coverImage = String.empty,
                format = CommonMediaEntry.Format.UNKNOWN,
                startDate = LocalDate.now(),
                endDate = null,
            ),
            chapters = null,
            volumes = null,
        )

        result shouldBeEqualToComparingFields MediaEntry.Manga(
            entry = entry.mediaEntry(),
            chapters = null,
            volumes = null,
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
            startDate = StartDate(null, null, null),
            endDate = EndDate(null, null, null),
            coverImage = MediaEntryFragment.CoverImage(null),
            nextAiringEpisode = null,
        )

        // WHEN
        val result = entry.mangaEntry()

        // THEN
        result shouldBeEqualToComparingFields MediaEntry.Manga(
            entry = CommonMediaEntry(
                id = Int.zero,
                title = String.empty,
                coverImage = String.empty,
                format = CommonMediaEntry.Format.UNKNOWN,
                startDate = LocalDate.now(),
                endDate = null,
            ),
            chapters = null,
            volumes = null,
        )

        result shouldBeEqualToComparingFields MediaEntry.Manga(
            entry = entry.mediaEntry(),
            chapters = null,
            volumes = null,
        )
    }

    @Test
    @DisplayName("WHEN an entry with all properties THEN it should be mapped to a `MediaEntry.Manga` class")
    fun `an entry with all properties`() = runTest {
        // GIVEN
        val entry = MediaEntryFragment(
            id = Int.zero,
            title = MediaEntryFragment.Title("One Piece"),
            episodes = null,
            chapters = 1046,
            volumes = 101,
            format = MediaFormat.MANGA,
            startDate = StartDate(2022, 1, 1),
            endDate = EndDate(2022, 12, 31),
            coverImage = MediaEntryFragment.CoverImage("https://placehold.co/128x256"),
            nextAiringEpisode = null,
        )

        // WHEN
        val result = entry.mangaEntry()

        // THEN
        result shouldBeEqualToComparingFields MediaEntry.Manga(
            entry = CommonMediaEntry(
                id = Int.zero,
                title = "One Piece",
                coverImage = "https://placehold.co/128x256",
                format = CommonMediaEntry.Format.MANGA,
                startDate = LocalDate.of(2022, 1, 1),
                endDate = LocalDate.of(2022, 12, 31),
            ),
            chapters = 1046,
            volumes = 101,
        )

        result shouldBeEqualToComparingFields MediaEntry.Manga(
            entry = entry.mediaEntry(),
            chapters = 1046,
            volumes = 101,
        )
    }
}
