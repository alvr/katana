package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.remote.type.MediaType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun Number.toLocalDateTime() = Instant.fromEpochSeconds(toLong())
    .toLocalDateTime(TimeZone.currentSystemDefault())

internal fun <R> MediaType.onMediaEntry(
    anime: () -> R,
    manga: () -> R,
): R = when (this) {
    MediaType.ANIME -> anime()
    MediaType.MANGA -> manga()
    else -> error("only MediaEntry.Anime and MediaEntry.Manga are accepted")
}
