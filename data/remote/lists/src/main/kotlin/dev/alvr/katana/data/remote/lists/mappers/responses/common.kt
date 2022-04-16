package dev.alvr.katana.data.remote.lists.mappers.responses

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal fun Number.toLocalDateTime() = LocalDateTime.ofInstant(
    Instant.ofEpochSecond(toLong()),
    ZoneId.systemDefault(),
)
