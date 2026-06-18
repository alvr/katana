package dev.alvr.katana.common.media.data.mappers.responses

import dev.alvr.katana.common.media.data.fragment.MediaEntry as MediaEntryFragment
import dev.alvr.katana.common.media.domain.models.ItemMediaId
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.core.remote.type.MediaFormat

internal fun MediaEntryFragment.mediaEntry() = let { entry ->
    CommonMediaEntry(
        id = ItemMediaId(entry.id),
        title = entry.title.userPreferred,
        coverImage = entry.coverImage.large,
        format = entry.format.toFormat(),
    )
}

private fun MediaFormat?.toFormat() =
    when (this) {
        MediaFormat.TV -> CommonMediaEntry.Format.TV
        MediaFormat.TV_SHORT -> CommonMediaEntry.Format.TV_SHORT
        MediaFormat.MOVIE -> CommonMediaEntry.Format.MOVIE
        MediaFormat.SPECIAL -> CommonMediaEntry.Format.SPECIAL
        MediaFormat.OVA -> CommonMediaEntry.Format.OVA
        MediaFormat.ONA -> CommonMediaEntry.Format.ONA
        MediaFormat.MUSIC -> CommonMediaEntry.Format.MUSIC
        MediaFormat.MANGA -> CommonMediaEntry.Format.MANGA
        MediaFormat.NOVEL -> CommonMediaEntry.Format.NOVEL
        MediaFormat.ONE_SHOT -> CommonMediaEntry.Format.ONE_SHOT
        MediaFormat.UNKNOWN__ -> CommonMediaEntry.Format.UNKNOWN
        null -> CommonMediaEntry.Format.UNKNOWN
    }
