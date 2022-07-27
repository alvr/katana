package dev.alvr.katana.data.remote.lists.mappers

import dev.alvr.katana.common.core.zero
import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaEntry
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal class MediaEntryMapperTest : WordSpec({
    "when the format is known" should {
        val formats = MediaFormat.knownValues().map { format ->
            MediaEntryFragment(
                id = Int.zero,
                title = null,
                episodes = null,
                duration = null,
                chapters = null,
                volumes = null,
                format = format,
                coverImage = null,
                genres = null,
                nextAiringEpisode = null,
            ).mediaEntry().format
        }

        "the format should not be UNKNOWN" {
            formats.shouldNotContain(CommonMediaEntry.Format.UNKNOWN)
        }
    }

    "the format is not known" `when` {
        "is unknown" should {
            val entryFormat = MediaEntryFragment(
                id = Int.zero,
                title = null,
                episodes = null,
                duration = null,
                chapters = null,
                volumes = null,
                format = MediaFormat.UNKNOWN__,
                coverImage = null,
                genres = null,
                nextAiringEpisode = null,
            ).mediaEntry().format

            "the format should be UNKNOWN" {
                entryFormat shouldBe CommonMediaEntry.Format.UNKNOWN
            }
        }

        "is null" should {
            val entryFormat = MediaEntryFragment(
                id = Int.zero,
                title = null,
                episodes = null,
                duration = null,
                chapters = null,
                volumes = null,
                format = null,
                coverImage = null,
                genres = null,
                nextAiringEpisode = null,
            ).mediaEntry().format

            "the format should be UNKNOWN" {
                entryFormat shouldBe CommonMediaEntry.Format.UNKNOWN
            }
        }
    }
},)
