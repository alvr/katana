package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment

internal class MangaEntryMapperTest : FreeSpec({
    "a null entry" {
        val entry: MediaEntryFragment? = null
        entry.mangaEntry().also { result ->
            result shouldBeEqual MediaEntry.Manga(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                chapters = null,
                volumes = null,
            )
            result shouldBeEqual MediaEntry.Manga(
                entry = entry.mediaEntry(),
                chapters = null,
                volumes = null,
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
        entry.mangaEntry().also { result ->
            result shouldBeEqual MediaEntry.Manga(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                chapters = null,
                volumes = null,
            )
            result shouldBeEqual MediaEntry.Manga(
                entry = entry.mediaEntry(),
                chapters = null,
                volumes = null,
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

        entry.mangaEntry().also { result ->
            result shouldBeEqual MediaEntry.Manga(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = String.empty,
                    coverImage = String.empty,
                    format = CommonMediaEntry.Format.UNKNOWN,
                ),
                chapters = null,
                volumes = null,
            )
            result shouldBeEqual MediaEntry.Manga(
                entry = entry.mediaEntry(),
                chapters = null,
                volumes = null,
            )
        }
    }

    "an entry with all properties" {
        val entry = MediaEntryFragment(
            id = Int.zero,
            title = MediaEntryFragment.Title("One Piece"),
            episodes = null,
            chapters = 1046,
            volumes = 101,
            format = MediaFormat.MANGA,
            coverImage = MediaEntryFragment.CoverImage("https://placehold.co/128x256"),
            nextAiringEpisode = null,
        )

        entry.mangaEntry().also { result ->
            result shouldBeEqual MediaEntry.Manga(
                entry = CommonMediaEntry(
                    id = Int.zero,
                    title = "One Piece",
                    coverImage = "https://placehold.co/128x256",
                    format = CommonMediaEntry.Format.MANGA,
                ),
                chapters = 1046,
                volumes = 101,
            )
            result shouldBeEqual MediaEntry.Manga(
                entry = entry.mediaEntry(),
                chapters = 1046,
                volumes = 101,
            )
        }
    }
})
