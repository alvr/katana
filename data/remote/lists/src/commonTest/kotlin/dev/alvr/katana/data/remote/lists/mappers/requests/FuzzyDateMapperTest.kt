package dev.alvr.katana.data.remote.lists.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.data.remote.base.type.FuzzyDateInput
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import korlibs.time.Date

internal class FuzzyDateMapperTest : FreeSpec() {
    private val possibleValues = listOf(
        Optional.Present(Arb.int(min = 1, max = 12).next()),
        Optional.Absent,
    )

    init {
        "a null Date" {
            val date: Date? = null

            date.toFuzzyDate() shouldBeEqual FuzzyDateInput(
                year = Optional.Absent,
                month = Optional.Absent,
                day = Optional.Absent,
            )
        }

        @Suppress("RedundantNullableReturnType")
        "a nullable Date with value" {
            val date: Date? = Date(2022, 7, 20)

            date.toFuzzyDate() shouldBeEqual FuzzyDateInput(
                year = Optional.Present(2022),
                month = Optional.Present(7),
                day = Optional.Present(20),
            )
        }

        "a valid Date" {
            Date(2022, 7, 20).toFuzzyDate() shouldBeEqual FuzzyDateInput(
                year = Optional.Present(2022),
                month = Optional.Present(7),
                day = Optional.Present(20),
            )
        }

        (possibleValues * possibleValues * possibleValues).forEach { (dayMonth, year) ->
            val (day, month) = dayMonth
            val (dayValue, monthValue, yearValue) = Triple(day.getOrNull(), month.getOrNull(), year.getOrNull())

            "date $dayValue/$monthValue/$yearValue with optional values" {
                val date = FuzzyDateInput(year, month, day)

                if (date.day is Optional.Present && date.month is Optional.Present && date.year is Optional.Present) {
                    date.takeIfValid().shouldNotBeNull()
                } else {
                    date.takeIfValid().shouldBeNull()
                }
            }
        }
    }

    private operator fun <S, T> List<S>.times(other: List<T>) = flatMap { list ->
        List(other.size) { list }.zip(other)
    }
}
