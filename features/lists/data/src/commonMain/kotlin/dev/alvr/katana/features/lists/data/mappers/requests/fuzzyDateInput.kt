package dev.alvr.katana.features.lists.data.mappers.requests

import dev.alvr.katana.core.remote.present
import dev.alvr.katana.core.remote.type.FuzzyDateInput
import korlibs.time.Date

internal fun Date.toFuzzyDate() = FuzzyDateInput(
    year = year.present,
    month = month1.present,
    day = day.present,
)
