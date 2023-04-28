package dev.alvr.katana.data.remote.lists.mappers.responses

import com.soywiz.klock.Date

internal fun dateMapper(day: Int?, month: Int?, year: Int?) =
    if (day != null && month != null && year != null) {
        Date(year, month, day)
    } else {
        null
    }
