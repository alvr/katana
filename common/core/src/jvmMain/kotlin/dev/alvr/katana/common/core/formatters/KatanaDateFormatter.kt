package dev.alvr.katana.common.core.formatters

import com.soywiz.klock.Date
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.Time
import dev.alvr.katana.common.core.noData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.time.temporal.TemporalAccessor

internal actual typealias DateFormatterBuilder = DateTimeFormatterBuilder

actual sealed class KatanaDateFormatter private actual constructor(
    private val builder: DateFormatterBuilder.() -> Unit
) {
    private val formatter: DateTimeFormatter
        get() = DateTimeFormatterBuilder()
            .apply(builder)
            .toFormatter()

    actual object DateWithTime : KatanaDateFormatter(
        {
            append(mediumDate)
            appendLiteral(" - ")
            append(shortTime)
        },
    )

    actual operator fun invoke(date: Date?) = date.toLocalDate().format()

    actual operator fun invoke(time: Time?) = time.toLocalTime().format()

    actual operator fun invoke(datetime: DateTime?) = datetime.toLocalDateTime().format()

    actual operator fun invoke(datetime: DateTimeTz?) = datetime.toLocalDateTime().format()

    private fun Date?.toLocalDate() = this?.let { date ->
        LocalDate.of(
            date.year,
            date.month1,
            date.day,
        )
    }

    private fun Time?.toLocalTime() = this?.let { time ->
        LocalTime.of(
            time.hour,
            time.minute,
            time.second,
            time.millisecond * TO_NANO,
        )
    }

    private fun DateTime?.toLocalDateTime() = this?.let { datetime ->
        LocalDateTime.of(
            datetime.yearInt,
            datetime.month1,
            datetime.dayOfMonth,
            datetime.hours,
            datetime.minutes,
            datetime.seconds,
            datetime.milliseconds * TO_NANO,
        )
    }

    private fun DateTimeTz?.toLocalDateTime() = this?.local.toLocalDateTime()

    private fun <T : TemporalAccessor> T?.format() =
        this?.let { formatter.format(it) } ?: String.noData

    private companion object {
        const val TO_NANO = 1000

        val mediumDate: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val shortTime: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    }
}
