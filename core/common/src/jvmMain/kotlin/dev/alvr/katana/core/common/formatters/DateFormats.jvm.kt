package dev.alvr.katana.core.common.formatters

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
actual object DateFormats {
    actual val mediumFormat = LocalDateTime.Format {
        byUnicodePattern(get(DateFormat.SHORT))
    }

    private fun get(format: Int) =
        (DateFormat.getDateInstance(format, Locale.getDefault()) as SimpleDateFormat).toLocalizedPattern()
}
