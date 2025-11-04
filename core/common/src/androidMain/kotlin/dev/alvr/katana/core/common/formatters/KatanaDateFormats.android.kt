package dev.alvr.katana.core.common.formatters

import dev.alvr.katana.core.common.locale.KatanaLocale
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle

internal actual typealias KatanaPlatformDateTimeFormatterBuilder = DateTimeFormatterBuilder

internal actual fun dateTimeFormatterBuilder(
    block: KatanaPlatformDateTimeFormatterBuilder.() -> Unit
): KatanaDateTimeFormatter = DateTimeFormatterBuilder()
    .apply(block)
    .toFormatter(KatanaLocale.default().locale)
    .let { KatanaDateTimeFormatter(it) }

actual object KatanaDateFormats {
    actual val nextEpisodeFormat: KatanaDateTimeFormatter
        get() = dateTimeFormatterBuilder {
            appendLocalized(FormatStyle.MEDIUM, null)
            appendLiteral(" - ")
            appendLocalized(null, FormatStyle.SHORT)
        }
}
