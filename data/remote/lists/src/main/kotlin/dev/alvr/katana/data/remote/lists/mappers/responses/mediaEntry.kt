package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

internal fun MediaEntryFragment?.mediaEntry() = let { entry ->
    MediaEntry(
        id = entry?.id ?: 0,
        title = entry?.title?.userPreferred.orEmpty(),
        coverImage = entry?.coverImage?.large.orEmpty(),
        format = entry?.format.toFormat(),
        genres = entry?.genres?.filterNotNull().orEmpty(),
    )
}

private fun MediaFormat?.toFormat() = when (this) {
    MediaFormat.TV -> MediaEntry.Format.TV
    MediaFormat.TV_SHORT -> MediaEntry.Format.TV_SHORT
    MediaFormat.MOVIE -> MediaEntry.Format.MOVIE
    MediaFormat.SPECIAL -> MediaEntry.Format.SPECIAL
    MediaFormat.OVA -> MediaEntry.Format.OVA
    MediaFormat.ONA -> MediaEntry.Format.ONA
    MediaFormat.MUSIC -> MediaEntry.Format.MUSIC
    MediaFormat.MANGA -> MediaEntry.Format.MANGA
    MediaFormat.NOVEL -> MediaEntry.Format.NOVEL
    MediaFormat.ONE_SHOT -> MediaEntry.Format.ONE_SHOT
    MediaFormat.UNKNOWN__ -> MediaEntry.Format.UNKNOWN
    null -> MediaEntry.Format.UNKNOWN
}
