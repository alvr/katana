package dev.alvr.katana.core.common.formatters

import dev.alvr.katana.core.common.locale.KatanaLocale
import java.time.format.DateTimeFormatter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime

actual typealias KatanaPlatformDateTimeFormatter = DateTimeFormatter

@JvmInline
actual value class KatanaDateTimeFormatter private constructor(
    private val formatter: KatanaPlatformDateTimeFormatter,
) {
    constructor(
        pattern: String,
        locale: KatanaLocale,
    ) : this(DateTimeFormatter.ofPattern(pattern, locale.locale))

    actual operator fun invoke(localDate: LocalDate): String =
        formatter.format(localDate.toJavaLocalDate())

    actual operator fun invoke(localTime: LocalTime): String =
        formatter.format(localTime.toJavaLocalTime())

    actual operator fun invoke(localDateTime: LocalDateTime): String =
        formatter.format(localDateTime.toJavaLocalDateTime())

    actual companion object {
        actual operator fun invoke(formatter: KatanaPlatformDateTimeFormatter): KatanaDateTimeFormatter =
            KatanaDateTimeFormatter(formatter)

        actual operator fun invoke(pattern: String, locale: KatanaLocale): KatanaDateTimeFormatter =
            KatanaDateTimeFormatter(pattern, locale)
    }
}
