package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.features.lists.data.fragment.MediaEntry
import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment

internal class MediaEntryMapperTest : FreeSpec({
    MediaFormat.knownEntries
        .forEach { format ->
            "MediaFormat $format should not be ${CommonMediaEntry.Format.UNKNOWN}" {
                MediaEntryFragment(
                    id = Int.zero,
                    title = MediaEntry.Title(String.empty),
                    episodes = null,
                    chapters = null,
                    volumes = null,
                    format = format,
                    coverImage = MediaEntry.CoverImage(String.empty),
                    nextAiringEpisode = null,
                ).mediaEntry().format shouldNotBe CommonMediaEntry.Format.UNKNOWN
            }
        }

    listOf(MediaFormat.UNKNOWN__, null)
        .forEach { format ->
            "MediaFormat $format should be ${CommonMediaEntry.Format.UNKNOWN}" {
                MediaEntryFragment(
                    id = Int.zero,
                    title = MediaEntry.Title(String.empty),
                    episodes = null,
                    chapters = null,
                    volumes = null,
                    format = format,
                    coverImage = MediaEntry.CoverImage(String.empty),
                    nextAiringEpisode = null,
                ).mediaEntry().format shouldBe CommonMediaEntry.Format.UNKNOWN
            }
        }
})
