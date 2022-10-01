package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.common.core.noData
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Manga
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Movie
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Music
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Novel
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Ona
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.OneShot
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Ova
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Special
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Tv
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.TvShort
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.Unknown
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import java.time.LocalDate
import java.time.LocalDateTime

internal class CommonListItemMapperTest : WordSpec() {
    init {
        "CommonMediaEntry.Format mapper" should {
            "map to the correct MediaListItem.Format" {
                infix fun CommonMediaEntry.Format.mappedTo(format: MediaListItem.Format) {
                    toEntity() shouldBe format
                }

                enumValues<CommonMediaEntry.Format>().forEach { format ->
                    when (format) {
                        CommonMediaEntry.Format.TV -> format mappedTo Tv
                        CommonMediaEntry.Format.TV_SHORT -> format mappedTo TvShort
                        CommonMediaEntry.Format.MOVIE -> format mappedTo Movie
                        CommonMediaEntry.Format.SPECIAL -> format mappedTo Special
                        CommonMediaEntry.Format.OVA -> format mappedTo Ova
                        CommonMediaEntry.Format.ONA -> format mappedTo Ona
                        CommonMediaEntry.Format.MUSIC -> format mappedTo Music
                        CommonMediaEntry.Format.MANGA -> format mappedTo Manga
                        CommonMediaEntry.Format.NOVEL -> format mappedTo Novel
                        CommonMediaEntry.Format.ONE_SHOT -> format mappedTo OneShot
                        CommonMediaEntry.Format.UNKNOWN -> format mappedTo Unknown
                    }
                }
            }
        }

        "MediaListItem mapper" should {
            "map of MediaList from a MediaListItem.AnimeListItem" {
                MediaListItem.AnimeListItem(
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
                    startedAt = LocalDate.of(2022, 7, 20),
                    completedAt = LocalDate.of(2022, 7, 20),
                    updatedAt = LocalDateTime.of(2022, 8, 14, 9, 0),
                    nextEpisode = null,
                ).toMediaList() shouldBeEqualToComparingFields MediaList(
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
            }

            "map of MediaList from a MediaListItem.MangaListItem" {
                MediaListItem.MangaListItem(
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
                    startedAt = LocalDate.of(2022, 7, 20),
                    completedAt = LocalDate.of(2022, 7, 20),
                    updatedAt = LocalDateTime.of(2022, 8, 14, 9, 0),
                ).toMediaList() shouldBeEqualToComparingFields MediaList(
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
            }
        }
    }
}
