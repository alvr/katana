package dev.alvr.katana.features.lists.data.mappers.responses

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import korlibs.time.Date

internal class DateMapperTest : FreeSpec({
    "valid date should be mapped to date" {
        dateMapper(13, 4, 2022)
            .shouldNotBeNull() shouldBeEqual Date(2022, 4, 13)
    }

    listOf(
        Triple(null, null, null),
        Triple(13, null, null),
        Triple(null, 4, null),
        Triple(null, null, 2022),
        Triple(13, 4, null),
        Triple(13, null, 2022),
        Triple(null, 4, 2022),
    ).forEach { (day, month, year) ->
        "invalid date $day/$month/$year should be mapped to null" {
            dateMapper(day, month, year).shouldBeNull()
        }
    }
})
