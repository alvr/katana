package dev.alvr.katana.features.lists.data.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.core.remote.type.FuzzyDateInput
import korlibs.time.Date

internal fun Date?.toFuzzyDate() = FuzzyDateInput(
    year = Optional.presentIfNotNull(this?.year),
    month = Optional.presentIfNotNull(this?.month1),
    day = Optional.presentIfNotNull(this?.day),
)

internal fun FuzzyDateInput.takeIfValid() = takeIf {
    year !is Optional.Absent && month !is Optional.Absent && day !is Optional.Absent
}
