package dev.alvr.katana.common.core.formatters

import korlibs.time.Date
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.Time

expect class DateFormatterBuilder

expect sealed class KatanaDateFormatter private constructor(builder: DateFormatterBuilder.() -> Unit) {
    object DateWithTime

    operator fun invoke(date: Date?): String

    operator fun invoke(time: Time?): String

    operator fun invoke(datetime: DateTime?): String

    operator fun invoke(datetime: DateTimeTz?): String
}
