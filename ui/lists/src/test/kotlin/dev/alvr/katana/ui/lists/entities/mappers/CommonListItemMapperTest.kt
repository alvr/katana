package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.common.core.noData
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Manga
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Tv
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Stream
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource

@ExperimentalCoroutinesApi
internal class CommonListItemMapperTest : TestBase() {
    @Test
    @DisplayName(
        """
        GIVEN a CommonMediaEntry.Format mapper
        WHEN mapping it
        THEN should map to the correct MediaListItem.Format
        """,
    )
    fun `a CommonMediaEntryFormat mapper`() = runTest {
        // GIVEN
        val formats = enumValues<CommonMediaEntry.Format>()

        // WHEN
        formats.forEach { format ->
            // THEN
            when (format) {
                CommonMediaEntry.Format.TV -> format mappedTo Tv
                CommonMediaEntry.Format.TV_SHORT -> format mappedTo MediaListItem.Format.TvShort
                CommonMediaEntry.Format.MOVIE -> format mappedTo MediaListItem.Format.Movie
                CommonMediaEntry.Format.SPECIAL -> format mappedTo MediaListItem.Format.Special
                CommonMediaEntry.Format.OVA -> format mappedTo MediaListItem.Format.Ova
                CommonMediaEntry.Format.ONA -> format mappedTo MediaListItem.Format.Ona
                CommonMediaEntry.Format.MUSIC -> format mappedTo MediaListItem.Format.Music
                CommonMediaEntry.Format.MANGA -> format mappedTo Manga
                CommonMediaEntry.Format.NOVEL -> format mappedTo MediaListItem.Format.Novel
                CommonMediaEntry.Format.ONE_SHOT -> format mappedTo MediaListItem.Format.OneShot
                CommonMediaEntry.Format.UNKNOWN -> format mappedTo MediaListItem.Format.Unknown
            }
        }
    }

    @ArgumentsSource(ItemsArguments::class)
    @ParameterizedTest(name = "GIVEN a MediaListItem mapper WHEN mapping {0} THEN should map to {1}")
    fun `a MediaListItem mapper`(input: MediaListItem, expected: MediaList) = runTest {
        // WHEN
        val result = input.toMediaList()

        // THEN
        result shouldBeEqualToComparingFields expected
    }

    private infix fun CommonMediaEntry.Format.mappedTo(format: MediaListItem.Format) {
        toEntity() shouldBe format
    }

    private class ItemsArguments : ArgumentsProvider {
        private val animeListItem = MediaListItem.AnimeListItem(
            entryId = 1234,
            mediaId = Arb.int().next(),
            title = Arb.string().next(),
            score = 9.8,
            format = Tv,
            cover = Arb.string().next(),
            progress = 132,
            total = null,
            repeat = 1,
            private = false,
            notes = String.noData,
            hiddenFromStatusLists = true,
            startDate = LocalDate.of(2022, 7, 20),
            endDate = LocalDate.of(2022, 7, 20),
            startedAt = LocalDate.of(2022, 7, 20),
            completedAt = LocalDate.of(2022, 7, 20),
            updatedAt = LocalDateTime.of(2022, 8, 14, 9, 0),
            nextEpisode = null,
        )
        private val mangaListItem = MediaListItem.MangaListItem(
            entryId = 5678,
            mediaId = Arb.int().next(),
            title = Arb.string().next(),
            score = 6.9,
            format = Manga,
            cover = Arb.string().next(),
            progress = 46,
            total = Arb.int().next(),
            volumesProgress = 12,
            volumesTotal = Arb.int().next(),
            repeat = Int.zero,
            private = true,
            notes = String.noData,
            hiddenFromStatusLists = false,
            startDate = LocalDate.MIN,
            endDate = LocalDate.MAX,
            startedAt = LocalDate.of(2022, 7, 20),
            completedAt = LocalDate.of(2022, 7, 20),
            updatedAt = LocalDateTime.of(2022, 8, 14, 9, 0),
        )

        private val animeMediaList = MediaList(
            id = 1234,
            score = 9.8,
            progress = 132,
            progressVolumes = null,
            repeat = 1,
            private = false,
            notes = String.noData,
            hiddenFromStatusLists = true,
            startedAt = LocalDate.of(2022, 7, 20),
            completedAt = LocalDate.of(2022, 7, 20),
            updatedAt = LocalDateTime.of(2022, 8, 14, 9, 0),
        )
        private val mangaMediaList = MediaList(
            id = 5678,
            score = 6.9,
            progress = 46,
            progressVolumes = 12,
            repeat = Int.zero,
            private = true,
            notes = String.noData,
            hiddenFromStatusLists = false,
            startedAt = LocalDate.of(2022, 7, 20),
            completedAt = LocalDate.of(2022, 7, 20),
            updatedAt = LocalDateTime.of(2022, 8, 14, 9, 0),
        )

        override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
            Stream.of(
                Arguments.of(animeListItem, animeMediaList),
                Arguments.of(mangaListItem, mangaMediaList),
            )
    }
}
