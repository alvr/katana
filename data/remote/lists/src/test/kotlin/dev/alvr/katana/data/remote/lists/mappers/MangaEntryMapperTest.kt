package dev.alvr.katana.data.remote.lists.mappers

import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.data.remote.lists.mappers.responses.mangaEntry
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaEntry
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal class MangaEntryMapperTest : WordSpec({
    "a null entry" should {
        val entry: MediaEntryFragment? = null

        "be mapped to the default values" {
            entry.mangaEntry() shouldBeEqualToComparingFields MediaEntry.Manga(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                chapters = null,
                volumes = null,
            )

            entry.mangaEntry() shouldBeEqualToComparingFields MediaEntry.Manga(
                entry = entry.mediaEntry(),
                chapters = null,
                volumes = null,
            )
        }
    }

    "an entry with null values" should {
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

        "be mapped to the default values" {
            entry.mangaEntry() shouldBeEqualToComparingFields MediaEntry.Manga(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                chapters = null,
                volumes = null,
            )

            entry.mangaEntry() shouldBeEqualToComparingFields MediaEntry.Manga(
                entry = entry.mediaEntry(),
                chapters = null,
                volumes = null,
            )
        }
    }

    "an entry with null values but data classes with null" should {
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

        "be mapped to the default values" {
            entry.mangaEntry() shouldBeEqualToComparingFields MediaEntry.Manga(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                chapters = null,
                volumes = null,
            )

            entry.mangaEntry() shouldBeEqualToComparingFields MediaEntry.Manga(
                entry = entry.mediaEntry(),
                chapters = null,
                volumes = null,
            )
        }
    }

    "an entry with all properties" should {
        val entry = MediaEntryFragment(
            id = Int.zero,
            title = MediaEntryFragment.Title("One Piece"),
            episodes = null,
            chapters = 1046,
            volumes = 101,
            format = MediaFormat.MANGA,
            coverImage = MediaEntryFragment.CoverImage("https://www.fillmurray.com/128/256"),
            nextAiringEpisode = null,
        )

        "be mapped to a `MediaEntry.Manga` class" {
            entry.mangaEntry() shouldBeEqualToComparingFields MediaEntry.Manga(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = "One Piece",
                    coverImage = "https://www.fillmurray.com/128/256",
                    format = CommonMediaEntry.Format.MANGA,
                ),
                chapters = 1046,
                volumes = 101,
            )

            entry.mangaEntry() shouldBeEqualToComparingFields MediaEntry.Manga(
                entry = entry.mediaEntry(),
                chapters = 1046,
                volumes = 101,
            )
        }
    }
},)
