package dev.alvr.katana.common.core

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("NOTHING_TO_INLINE")
inline fun LocalDate?.orNow() = this ?: LocalDate.now()

@Suppress("NOTHING_TO_INLINE")
inline fun LocalTime?.orNow() = this ?: LocalTime.now()

@Suppress("NOTHING_TO_INLINE")
inline fun LocalDateTime?.orNow() = this ?: LocalDateTime.now()
