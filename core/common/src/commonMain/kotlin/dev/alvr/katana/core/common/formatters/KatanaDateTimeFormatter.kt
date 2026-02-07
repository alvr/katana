package dev.alvr.katana.core.common.formatters

import dev.alvr.katana.core.common.locale.KatanaLocale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

expect class KatanaPlatformDateTimeFormatter

expect value class KatanaDateTimeFormatter private constructor(private val formatter: KatanaPlatformDateTimeFormatter) {
    operator fun invoke(localDate: LocalDate): String

    operator fun invoke(localTime: LocalTime): String

    operator fun invoke(localDateTime: LocalDateTime): String

    companion object {
        operator fun invoke(formatter: KatanaPlatformDateTimeFormatter): KatanaDateTimeFormatter

        operator fun invoke(pattern: String, locale: KatanaLocale = KatanaLocale.default()): KatanaDateTimeFormatter
    }
}
