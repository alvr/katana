package dev.alvr.katana.ui.base.components.datepicker

import android.os.Parcelable
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.parcelize.Parcelize

@Parcelize
data class KatanaDatePickerParams(
    val selectedDay: LocalDate?,
    val startMonth: YearMonth,
    val endMonth: YearMonth,
    val firstVisibleMonth: YearMonth,
    val title: String? = null,
) : Parcelable
