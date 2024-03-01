package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.common.orZero
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.data.fragment.MediaEntry as MediaEntryFragment

internal fun MediaEntryFragment?.mediaEntry() = let { entry ->
    CommonMediaEntry(
        id = entry?.id.orZero(),
        title = entry?.title?.userPreferred.orEmpty(),
        coverImage = entry?.coverImage?.large.orEmpty(),
        format = entry?.format.toFormat(),
    )
}

private fun MediaFormat?.toFormat() = when (this) {
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
