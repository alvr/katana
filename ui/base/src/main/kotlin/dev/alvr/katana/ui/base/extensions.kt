package dev.alvr.katana.ui.base

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import java.time.LocalDate
import java.time.YearMonth

val currentLocale = @Composable {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        LocalConfiguration.current.locales[0]
    } else {
        @Suppress("DEPRECATION")
        LocalConfiguration.current.locale
    }
}

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)
