package dev.alvr.katana.common.media.data.mappers.responses

import kotlinx.datetime.LocalDate

internal fun dateMapper(day: Int?, month: Int?, year: Int?) =
    if (day != null && month != null && year != null) {
        LocalDate(year, month, day)
    } else {
        null
    }
