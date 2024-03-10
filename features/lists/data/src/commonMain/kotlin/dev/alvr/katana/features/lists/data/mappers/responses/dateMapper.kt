package dev.alvr.katana.features.lists.data.mappers.responses

import kotlinx.datetime.LocalDate

internal fun dateMapper(day: Int?, month: Int?, year: Int?) =
    if (day != null && month != null && year != null) {
        LocalDate(year = year, monthNumber = month, dayOfMonth = day)
    } else {
        null
    }
