package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment
import dev.alvr.katana.features.lists.domain.models.ItemMediaId
import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual

internal class MangaEntryMapperTest :
    FreeSpec({
        "an entry with null values" {
            val entry =
                MediaEntryFragment(
                    __typename = "MediaEntry",
                    id = Int.zero,
                    title = MediaEntryFragment.Title(__typename = "MediaEntryTitle", userPreferred = String.empty),
                    episodes = null,
                    chapters = null,
                    volumes = null,
                    format = null,
                    coverImage =
                        MediaEntryFragment.CoverImage(__typename = "MediaEntryCoverImage", large = String.empty),
                    nextAiringEpisode = null,
                )
            entry.mangaEntry().also { result ->
                result shouldBeEqual
                    MediaEntry.Manga(
                        entry =
                            CommonMediaEntry(
                                id = ItemMediaId(Int.zero),
                                title = String.empty,
                                coverImage = String.empty,
                                format = CommonMediaEntry.Format.UNKNOWN,
                            ),
                        chapters = null,
                        volumes = null,
                    )
                result shouldBeEqual MediaEntry.Manga(entry = entry.mediaEntry(), chapters = null, volumes = null)
            }
        }

        "an entry with null values but data classes with null" {
            val entry =
                MediaEntryFragment(
                    __typename = "MediaEntry",
                    id = Int.zero,
                    title = MediaEntryFragment.Title(__typename = "MediaEntryTitle", userPreferred = String.empty),
                    episodes = null,
                    chapters = null,
                    volumes = null,
                    format = null,
                    coverImage =
                        MediaEntryFragment.CoverImage(__typename = "MediaEntryCoverImage", large = String.empty),
                    nextAiringEpisode = null,
                )

            entry.mangaEntry().also { result ->
                result shouldBeEqual
                    MediaEntry.Manga(
                        entry =
                            CommonMediaEntry(
                                id = ItemMediaId(Int.zero),
                                title = String.empty,
                                coverImage = String.empty,
                                format = CommonMediaEntry.Format.UNKNOWN,
                            ),
                        chapters = null,
                        volumes = null,
                    )
                result shouldBeEqual MediaEntry.Manga(entry = entry.mediaEntry(), chapters = null, volumes = null)
            }
        }

        "an entry with all properties" {
            val entry =
                MediaEntryFragment(
                    __typename = "MediaEntry",
                    id = Int.zero,
                    title = MediaEntryFragment.Title(__typename = "MediaEntryTitle", userPreferred = "One Piece"),
                    episodes = null,
                    chapters = 1046,
                    volumes = 101,
                    format = MediaFormat.MANGA,
                    coverImage =
                        MediaEntryFragment.CoverImage(
                            __typename = "MediaEntryCoverImage",
                            large = "https://placehold.co/128x256",
                        ),
                    nextAiringEpisode = null,
                )

            entry.mangaEntry().also { result ->
                result shouldBeEqual
                    MediaEntry.Manga(
                        entry =
                            CommonMediaEntry(
                                id = ItemMediaId(Int.zero),
                                title = "One Piece",
                                coverImage = "https://placehold.co/128x256",
                                format = CommonMediaEntry.Format.MANGA,
                            ),
                        chapters = 1046,
                        volumes = 101,
                    )
                result shouldBeEqual MediaEntry.Manga(entry = entry.mediaEntry(), chapters = 1046, volumes = 101)
            }
        }
    })
