package dev.alvr.katana.data.remote.lists.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.data.remote.base.type.FuzzyDateInput
import java.time.LocalDate

internal fun LocalDate?.toFuzzyDate() = FuzzyDateInput(
    year = Optional.presentIfNotNull(this?.year),
    month = Optional.presentIfNotNull(this?.monthValue),
    day = Optional.presentIfNotNull(this?.dayOfMonth),
)
