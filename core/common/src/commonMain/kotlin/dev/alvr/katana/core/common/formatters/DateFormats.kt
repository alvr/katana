package dev.alvr.katana.core.common.formatters

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

expect object DateFormats {
    val mediumFormat: DateTimeFormat<LocalDateTime>
}
