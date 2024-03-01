package dev.alvr.katana.features.lists.data.mappers.responses

import korlibs.time.Date

internal fun dateMapper(day: Int?, month: Int?, year: Int?) =
    if (day != null && month != null && year != null) {
        Date(year, month, day)
    } else {
        null
    }
