package dev.alvr.katana.domain.base.formatters

import dev.alvr.katana.common.core.noData
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.time.temporal.TemporalAccessor

sealed class KatanaDateFormatter(private val format: DateTimeFormatterBuilder.() -> Unit) {
    object DateWithTime : KatanaDateFormatter(
        {
            append(mediumDate)
            appendLiteral(" - ")
            append(shortTime)
        },
    )

    object MediumDate : KatanaDateFormatter({ append(mediumDate) })

    private val formatter: DateTimeFormatter
        get() = DateTimeFormatterBuilder()
            .apply(format)
            .toFormatter()

    operator fun <T : TemporalAccessor> invoke(date: T?) =
        date?.let { formatter.format(it) } ?: String.noData

    private companion object {
        val mediumDate: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val shortTime: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    }
}
