@file:Suppress("StringLiteralDuplication")

package dev.alvr.katana.core.common.formatters

import dev.alvr.katana.core.common.locale.KatanaLocale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

actual value class KatanaDateTimeFormatter private actual constructor(
    private val formatter: KatanaPlatformDateTimeFormatter,
) {
    actual operator fun invoke(localDate: LocalDate): String {
        TODO("Not yet implemented")
    }

    actual operator fun invoke(localTime: LocalTime): String {
        TODO("Not yet implemented")
    }

    actual operator fun invoke(localDateTime: LocalDateTime): String {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual operator fun invoke(formatter: KatanaPlatformDateTimeFormatter): KatanaDateTimeFormatter {
            TODO("Not yet implemented")
        }

        actual operator fun invoke(
            pattern: String,
            locale: KatanaLocale
        ): KatanaDateTimeFormatter {
            TODO("Not yet implemented")
        }
    }
}

actual class KatanaPlatformDateTimeFormatter
