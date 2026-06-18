package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.common.media.domain.models.ItemEntryId
import dev.alvr.katana.common.media.domain.models.ItemMediaId
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.core.common.noData
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

internal class CommonListItemMapperTest : FreeSpec() {

    private val animeListItem =
        MediaListItem.AnimeListItem(
            entryId = ItemEntryId(1234),
            mediaId = ItemMediaId(Arb.int().next()),
            title = Arb.string().next(),
            score = 9.8,
            format = MediaListItem.Format.Tv,
            cover = Arb.string().next(),
            progress = 132,
            total = null,
            repeat = 1,
            private = false,
            notes = String.noData,
            hiddenFromStatusLists = true,
            startedAt = LocalDate(2022, 7, 20),
            completedAt = LocalDate(2022, 7, 20),
            updatedAt = LocalDateTime(2022, 8, 14, 9, 0),
            nextEpisode = null,
        )
    private val mangaListItem =
        MediaListItem.MangaListItem(
            entryId = ItemEntryId(5678),
            mediaId = ItemMediaId(Arb.int().next()),
            title = Arb.string().next(),
            score = 6.9,
            format = MediaListItem.Format.Manga,
            cover = Arb.string().next(),
            progress = 46,
            total = Arb.int().next(),
            volumesProgress = 12,
            volumesTotal = Arb.int().next(),
            repeat = Int.zero,
            private = true,
            notes = String.noData,
            hiddenFromStatusLists = false,
            startedAt = LocalDate(2022, 7, 20),
            completedAt = LocalDate(2022, 7, 20),
            updatedAt = LocalDateTime(2022, 8, 14, 9, 0),
        )

    private val animeMediaList =
        MediaList(
            id = ItemEntryId(1234),
            score = 9.8,
            progress = 132,
            progressVolumes = null,
            repeat = 1,
            private = false,
            notes = String.noData,
            hiddenFromStatusLists = true,
            startedAt = LocalDate(2022, 7, 20),
            completedAt = LocalDate(2022, 7, 20),
            updatedAt = LocalDateTime(2022, 8, 14, 9, 0),
        )
    private val mangaMediaList =
        MediaList(
            id = ItemEntryId(5678),
            score = 6.9,
            progress = 46,
            progressVolumes = 12,
            repeat = Int.zero,
            private = true,
            notes = String.noData,
            hiddenFromStatusLists = false,
            startedAt = LocalDate(2022, 7, 20),
            completedAt = LocalDate(2022, 7, 20),
            updatedAt = LocalDateTime(2022, 8, 14, 9, 0),
        )

    init {
        "a CommonMediaEntryFormat mapper" {
            CommonMediaEntry.Format.entries.forEach { format ->
                when (format) {
                    CommonMediaEntry.Format.TV -> format mappedTo MediaListItem.Format.Tv
                    CommonMediaEntry.Format.TV_SHORT -> format mappedTo MediaListItem.Format.TvShort
                    CommonMediaEntry.Format.MOVIE -> format mappedTo MediaListItem.Format.Movie
                    CommonMediaEntry.Format.SPECIAL -> format mappedTo MediaListItem.Format.Special
                    CommonMediaEntry.Format.OVA -> format mappedTo MediaListItem.Format.Ova
                    CommonMediaEntry.Format.ONA -> format mappedTo MediaListItem.Format.Ona
                    CommonMediaEntry.Format.MUSIC -> format mappedTo MediaListItem.Format.Music
                    CommonMediaEntry.Format.MANGA -> format mappedTo MediaListItem.Format.Manga
                    CommonMediaEntry.Format.NOVEL -> format mappedTo MediaListItem.Format.Novel
                    CommonMediaEntry.Format.ONE_SHOT -> format mappedTo MediaListItem.Format.OneShot
                    CommonMediaEntry.Format.UNKNOWN -> format mappedTo MediaListItem.Format.Unknown
                }
            }
        }

        listOf(animeListItem to animeMediaList, mangaListItem to mangaMediaList).forEach { (input, expected) ->
            "a $input should be mapped to $expected" { input.toMediaList() shouldBeEqual expected }
        }
    }

    private infix fun CommonMediaEntry.Format.mappedTo(format: MediaListItem.Format) {
        toEntity() shouldBe format
    }
}
