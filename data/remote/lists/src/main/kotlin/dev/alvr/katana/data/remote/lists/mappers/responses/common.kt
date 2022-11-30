package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.data.remote.base.type.MediaType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal fun Number.toLocalDateTime() = LocalDateTime.ofInstant(
    Instant.ofEpochSecond(toLong()),
    ZoneId.systemDefault(),
)

internal fun <R> MediaType.onMediaEntry(
    anime: () -> R,
    manga: () -> R,
): R = when (this) {
    MediaType.ANIME -> anime()
    MediaType.MANGA -> manga()
    else -> error("only MediaEntry.Anime and MediaEntry.Manga are accepted")
}
