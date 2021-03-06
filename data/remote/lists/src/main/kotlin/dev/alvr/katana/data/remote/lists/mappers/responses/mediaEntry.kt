package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal fun MediaEntryFragment?.mediaEntry() = let { entry ->
    CommonMediaEntry(
        id = entry?.id ?: 0,
        title = entry?.title?.userPreferred.orEmpty(),
        coverImage = entry?.coverImage?.large.orEmpty(),
        format = entry?.format.toFormat(),
        genres = entry?.genres?.filterNotNull().orEmpty(),
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
