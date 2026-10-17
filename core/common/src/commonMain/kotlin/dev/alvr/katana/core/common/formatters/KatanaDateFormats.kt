package dev.alvr.katana.core.common.formatters

expect class KatanaPlatformDateTimeFormatterBuilder

internal expect fun dateTimeFormatterBuilder(
    block: KatanaPlatformDateTimeFormatterBuilder.() -> Unit,
): KatanaDateTimeFormatter

expect object KatanaDateFormats {
    val nextEpisodeFormat: KatanaDateTimeFormatter
}
