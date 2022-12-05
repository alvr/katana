package dev.alvr.katana.ui.base.components.datepicker

import java.io.Serializable
import java.time.LocalDate
import java.time.YearMonth

data class KatanaDatePickerParams(
    val selectedDay: LocalDate?,
    val startMonth: YearMonth,
    val endMonth: YearMonth,
    val firstVisibleMonth: YearMonth,
    val title: String? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = -848355768L
    }
}
