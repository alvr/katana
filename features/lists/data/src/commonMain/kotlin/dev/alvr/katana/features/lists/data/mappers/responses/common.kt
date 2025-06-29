package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.remote.type.MediaType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
internal fun Number.toLocalDateTime() = Instant
    .fromEpochMilliseconds(toLong() * TO_UNIX)
    .toLocalDateTime(TimeZone.UTC)

internal fun <R> MediaType.onMediaEntry(
    anime: () -> R,
    manga: () -> R,
): R = when (this) {
    MediaType.ANIME -> anime()
    MediaType.MANGA -> manga()
    else -> error("only MediaEntry.Anime and MediaEntry.Manga are accepted")
}

private const val TO_UNIX = 1000
