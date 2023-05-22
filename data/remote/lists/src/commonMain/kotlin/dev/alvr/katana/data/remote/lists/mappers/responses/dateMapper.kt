package dev.alvr.katana.data.remote.lists.mappers.responses

import korlibs.time.Date

internal fun dateMapper(day: Int?, month: Int?, year: Int?) =
    if (day != null && month != null && year != null) {
        Date(year, month, day)
    } else {
        null
    }
