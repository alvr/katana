package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.common.core.zero
import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal class MediaEntryMapperTest : FreeSpec({
    MediaFormat.knownValues()
        .forEach { format ->
            "MediaFormat $format should not be ${CommonMediaEntry.Format.UNKNOWN}" {
                MediaEntryFragment(
                    id = Int.zero,
                    title = null,
                    episodes = null,
                    chapters = null,
                    volumes = null,
                    format = format,
                    coverImage = null,
                    nextAiringEpisode = null,
                ).mediaEntry().format shouldNotBe CommonMediaEntry.Format.UNKNOWN
            }
        }

    listOf(MediaFormat.UNKNOWN__, null)
        .forEach { format ->
            "MediaFormat $format should be ${CommonMediaEntry.Format.UNKNOWN}" {
                MediaEntryFragment(
                    id = Int.zero,
                    title = null,
                    episodes = null,
                    chapters = null,
                    volumes = null,
                    format = format,
                    coverImage = null,
                    nextAiringEpisode = null,
                ).mediaEntry().format shouldBe CommonMediaEntry.Format.UNKNOWN
            }
        }
})
