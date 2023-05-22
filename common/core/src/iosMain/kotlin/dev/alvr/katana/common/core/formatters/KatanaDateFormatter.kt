package dev.alvr.katana.common.core.formatters

import dev.alvr.katana.common.core.noData
import korlibs.time.Date
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.Time
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.dateWithTimeIntervalSince1970

actual typealias DateFormatterBuilder = NSDateFormatter

actual sealed class KatanaDateFormatter private actual constructor(
    private val builder: DateFormatterBuilder.() -> Unit
) {
    private val formatter: NSDateFormatter
        get() = NSDateFormatter().apply(builder)

    actual object DateWithTime : KatanaDateFormatter(
        {
            dateStyle = NSDateFormatterMediumStyle
            timeStyle = NSDateFormatterShortStyle
        },
    )

    actual operator fun invoke(date: Date?) = date.toNSDate().format()

    actual operator fun invoke(time: Time?) = time.toNSDate().format()

    actual operator fun invoke(datetime: DateTime?) = datetime.toNSDate().format()

    actual operator fun invoke(datetime: DateTimeTz?) = datetime.toNSDate().format()

    private fun Date?.toNSDate() = this?.dateTimeDayStart.toNSDate()

    private fun Time?.toNSDate() = this?.let {
        NSDate.dateWithTimeIntervalSince1970((second + millisecond / TO_SECONDS).toDouble())
    }

    private fun DateTime?.toNSDate() = this?.let {
        NSDate.dateWithTimeIntervalSince1970((seconds + milliseconds / TO_SECONDS).toDouble())
    }

    private fun DateTimeTz?.toNSDate() = this?.local?.toNSDate()

    private fun NSDate?.format() = this?.let { formatter.stringFromDate(it) } ?: String.noData

    private companion object {
        const val TO_SECONDS = 1000
    }
}
