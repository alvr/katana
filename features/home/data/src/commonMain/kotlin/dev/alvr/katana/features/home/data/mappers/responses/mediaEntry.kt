package dev.alvr.katana.features.home.data.mappers.responses

import dev.alvr.katana.common.media.domain.models.ItemMediaId
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.features.home.data.fragment.HomeMediaEntry

internal fun HomeMediaEntry.toMediaEntry() =
    CommonMediaEntry(
        id = ItemMediaId(id),
        title = title.userPreferred,
        coverImage = coverImage.large,
        format = format.toFormat(),
    )

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
