package dev.alvr.katana.ui.base

import androidx.compose.runtime.Composable
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle

private val mediumDate: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
private val shortTime: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

enum class KatanaDateFormatter(private val format: DateTimeFormatterBuilder.() -> Unit) {
    DATE_WITH_TIME(
        {
            append(mediumDate)
            appendLiteral(" - ")
            append(shortTime)
        },
    ),

    MEDIUM_DATE({ append(mediumDate) });

    @Composable
    @Suppress("TopLevelComposableFunctions")
    internal operator fun invoke(): DateTimeFormatter = DateTimeFormatterBuilder()
        .apply(format)
        .toFormatter(currentLocale())
}
