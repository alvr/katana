package dev.alvr.katana.features.lists.data.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.core.remote.type.FuzzyDateInput
import kotlinx.datetime.LocalDate

internal fun LocalDate?.toFuzzyDate() = FuzzyDateInput(
    year = Optional.presentIfNotNull(this?.year),
    month = Optional.presentIfNotNull(this?.monthNumber),
    day = Optional.presentIfNotNull(this?.dayOfMonth),
)

internal fun FuzzyDateInput.takeIfValid() = takeIf {
    year !is Optional.Absent && month !is Optional.Absent && day !is Optional.Absent
}
