package dev.alvr.katana.core.common.formatters

import dev.alvr.katana.core.common.noData
import kotlin.jvm.JvmInline
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat

@JvmInline
value class KatanaDateFormatter private constructor(private val format: DateTimeFormat<*>) {

    @Suppress("UNCHECKED_CAST")
    operator fun invoke(date: LocalDate?) = date?.format(format as DateTimeFormat<LocalDate>) ?: String.noData

    @Suppress("UNCHECKED_CAST")
    operator fun invoke(dateTime: LocalDateTime?) = dateTime?.format(format as DateTimeFormat<LocalDateTime>) ?: String.noData

    companion object {
        val DateWithTime = KatanaDateFormatter(DateFormats.mediumFormat)
    }
}

internal expect fun currentLanguageCode(): String
