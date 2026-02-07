package dev.alvr.katana.features.lists.data.mappers.requests

import com.apollographql.apollo.api.Optional
import dev.alvr.katana.core.remote.type.FuzzyDateInput
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.datetime.LocalDate

internal class FuzzyDateMapperTest :
    FreeSpec({
        "a null Date" {
            val date: LocalDate? = null

            date?.toFuzzyDate().shouldBeNull()
        }

        @Suppress("RedundantNullableReturnType")
        "a nullable Date with value" {
            val date: LocalDate? = LocalDate(2022, 7, 20)

            date?.toFuzzyDate().shouldNotBeNull() shouldBeEqual
                FuzzyDateInput(year = Optional.Present(2022), month = Optional.Present(7), day = Optional.Present(20))
        }

        "a valid Date" {
            LocalDate(2022, 7, 20).toFuzzyDate().shouldNotBeNull() shouldBeEqual
                FuzzyDateInput(year = Optional.Present(2022), month = Optional.Present(7), day = Optional.Present(20))
        }
    })
