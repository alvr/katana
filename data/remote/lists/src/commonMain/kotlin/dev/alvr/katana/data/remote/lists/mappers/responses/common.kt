package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.base.type.MediaType
import korlibs.time.DateTimeTz

internal fun Number.toLocalDateTime() = DateTimeTz.fromUnix(toLong() * TO_UNIX)

internal fun <R> MediaType.onMediaEntry(
    anime: () -> R,
    manga: () -> R,
): R = when (this) {
    MediaType.ANIME -> anime()
    MediaType.MANGA -> manga()
    else -> error("only MediaEntry.Anime and MediaEntry.Manga are accepted")
}

private const val TO_UNIX = 1000
