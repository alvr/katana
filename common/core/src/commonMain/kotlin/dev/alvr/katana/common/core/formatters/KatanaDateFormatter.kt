package dev.alvr.katana.common.core.formatters

import com.soywiz.klock.Date
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.Time

expect class DateFormatterBuilder

expect sealed class KatanaDateFormatter private constructor(builder: DateFormatterBuilder.() -> Unit) {
    object DateWithTime

    operator fun invoke(date: Date?): String

    operator fun invoke(time: Time?): String

    operator fun invoke(datetime: DateTime?): String

    operator fun invoke(datetime: DateTimeTz?): String
}
