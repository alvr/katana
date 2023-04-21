package dev.alvr.katana.data.remote.lists.mappers.responses

import java.time.LocalDate

internal fun dateMapper(day: Int?, month: Int?, year: Int?) =
    if (day != null && month != null && year != null) {
        LocalDate.of(year, month, day)
    } else {
        null
    }
