package dev.alvr.katana.features.lists.data.mappers.requests

import dev.alvr.katana.core.remote.present
import dev.alvr.katana.core.remote.type.FuzzyDateInput
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

internal fun LocalDate.toFuzzyDate() = FuzzyDateInput(
    year = year.present,
    month = month.number.present,
    day = day.present,
)
