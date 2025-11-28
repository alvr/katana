package dev.alvr.katana.core.common.formatters

import dev.alvr.katana.core.common.locale.KatanaLocale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import platform.Foundation.NSDateFormatter

actual typealias KatanaPlatformDateTimeFormatter = NSDateFormatter

actual value class KatanaDateTimeFormatter private constructor(private val formatter: KatanaPlatformDateTimeFormatter) {
    actual operator fun invoke(localDate: LocalDate): String = TODO()
    actual operator fun invoke(localTime: LocalTime): String = TODO()
    actual operator fun invoke(localDateTime: LocalDateTime): String = TODO()

    actual companion object {
        actual operator fun invoke(formatter: KatanaPlatformDateTimeFormatter): KatanaDateTimeFormatter =
            KatanaDateTimeFormatter(formatter)

        actual operator fun invoke(
            pattern: String,
            locale: KatanaLocale
        ): KatanaDateTimeFormatter = TODO()
    }
}
