package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.core.tests.random
import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment
import dev.alvr.katana.features.lists.domain.models.ItemMediaId
import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import kotlinx.datetime.LocalDateTime

internal class AnimeEntryMapperTest :
    FreeSpec({
        "an entry with null values" {
            val entry =
                MediaEntryFragment(
                    __typename = String.random,
                    id = Int.zero,
                    title = MediaEntryFragment.Title(String.empty),
                    episodes = null,
                    chapters = null,
                    volumes = null,
                    format = null,
                    coverImage = MediaEntryFragment.CoverImage(String.empty),
                    nextAiringEpisode = null,
                )
            entry.animeEntry().also { result ->
                result shouldBeEqual
                    MediaEntry.Anime(
                        entry =
                            CommonMediaEntry(
                                id = ItemMediaId(Int.zero),
                                title = String.empty,
                                coverImage = String.empty,
                                format = CommonMediaEntry.Format.UNKNOWN,
                            ),
                        episodes = null,
                        nextEpisode = null,
                    )
                result shouldBeEqual MediaEntry.Anime(entry = entry.mediaEntry(), episodes = null, nextEpisode = null)
            }
        }

        "an entry with null values but data classes with null" {
            val entry =
                MediaEntryFragment(
                    __typename = String.random,
                    id = Int.zero,
                    title = MediaEntryFragment.Title(String.empty),
                    episodes = null,
                    chapters = null,
                    volumes = null,
                    format = null,
                    coverImage = MediaEntryFragment.CoverImage(String.empty),
                    nextAiringEpisode = null,
                )

            entry.animeEntry().also { result ->
                result shouldBeEqual
                    MediaEntry.Anime(
                        entry =
                            CommonMediaEntry(
                                id = ItemMediaId(Int.zero),
                                title = String.empty,
                                coverImage = String.empty,
                                format = CommonMediaEntry.Format.UNKNOWN,
                            ),
                        episodes = null,
                        nextEpisode = null,
                    )
                result shouldBeEqual MediaEntry.Anime(entry = entry.mediaEntry(), episodes = null, nextEpisode = null)
            }
        }

        "an entry with all properties" {
            val entry =
                MediaEntryFragment(
                    __typename = String.random,
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
                result shouldBeEqual
                    MediaEntry.Anime(
                        entry =
                            CommonMediaEntry(
                                id = ItemMediaId(Int.zero),
                                title = "One Piece",
                                coverImage = "https://placehold.co/128x256",
                                format = CommonMediaEntry.Format.ONA,
                            ),
                        episodes = 1000,
                        nextEpisode = MediaEntry.Anime.NextEpisode(1001, LocalDateTime(2009, 5, 5, 10, 0, 0)),
                    )
                result shouldBeEqual
                    MediaEntry.Anime(
                        entry = entry.mediaEntry(),
                        episodes = 1000,
                        nextEpisode = MediaEntry.Anime.NextEpisode(1001, LocalDateTime(2009, 5, 5, 10, 0, 0)),
                    )
            }
        }
    })
