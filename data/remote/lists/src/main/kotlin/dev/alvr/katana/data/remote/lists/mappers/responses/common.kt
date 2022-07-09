package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal fun Number.toLocalDateTime() = LocalDateTime.ofInstant(
    Instant.ofEpochSecond(toLong()),
    ZoneId.systemDefault(),
)

internal inline fun <reified T : MediaEntry, R> onMediaEntry(
    anime: () -> R,
    manga: () -> R,
): R = when (T::class) {
    MediaEntry.Anime::class -> anime()
    MediaEntry.Manga::class -> manga()
    else -> error("only MediaEntry.Anime and MediaEntry.Manga are accepted")
}
