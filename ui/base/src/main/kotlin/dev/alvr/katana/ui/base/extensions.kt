package dev.alvr.katana.ui.base

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import dev.alvr.katana.common.core.noData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

val currentLocale = @Composable {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        LocalConfiguration.current.locales[0]
    } else {
        @Suppress("DEPRECATION")
        LocalConfiguration.current.locale
    }
}

val LocalDate?.yearMonth: YearMonth
    get() = if (this?.year != null && month != null) YearMonth.of(year, month) else YearMonth.now()

@Composable
fun LocalDate?.format(formatter: KatanaDateFormatter) =
    formatter().let { this?.format(it) ?: String.noData }

@Composable
fun LocalTime?.format(formatter: KatanaDateFormatter) =
    formatter().let { this?.format(it) ?: String.noData }

@Composable
fun LocalDateTime?.format(formatter: KatanaDateFormatter) =
    formatter().let { this?.format(it) ?: String.noData }
