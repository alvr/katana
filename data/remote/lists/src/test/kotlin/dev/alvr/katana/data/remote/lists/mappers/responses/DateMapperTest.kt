package dev.alvr.katana.data.remote.lists.mappers.responses

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldBeNull
import java.time.LocalDate

internal class DateMapperTest : StringSpec() {
    init {
        "all null" {
            dateMapper(null, null, null).shouldBeNull()
        }

        "month and year null" {
            dateMapper(13, null, null).shouldBeNull()
        }

        "day and year null" {
            dateMapper(null, 4, null).shouldBeNull()
        }

        "day and month null" {
            dateMapper(null, null, 2022).shouldBeNull()
        }

        "only year null" {
            dateMapper(13, 4, null).shouldBeNull()
        }

        "only month null" {
            dateMapper(13, null, 2022).shouldBeNull()
        }

        "only day null" {
            dateMapper(null, 4, 2022).shouldBeNull()
        }

        "no nulls" {
            dateMapper(13, 4, 2022)?.shouldBeEqualComparingTo(LocalDate.of(2022, 4, 13))
        }
    }
}
