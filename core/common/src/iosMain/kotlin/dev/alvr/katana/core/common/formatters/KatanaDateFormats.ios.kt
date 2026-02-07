package dev.alvr.katana.core.common.formatters

import platform.Foundation.NSDateFormatter

internal actual typealias KatanaPlatformDateTimeFormatterBuilder = NSDateFormatter

internal actual fun dateTimeFormatterBuilder(
    block: KatanaPlatformDateTimeFormatterBuilder.() -> Unit
): KatanaDateTimeFormatter = KatanaDateTimeFormatter(NSDateFormatter().apply(block))

actual object KatanaDateFormats {
    actual val nextEpisodeFormat: KatanaDateTimeFormatter
        get() = TODO()
}
