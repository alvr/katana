package dev.alvr.katana.data.remote.lists.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.data.remote.base.type.FuzzyDateInput
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.edgecases
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import java.time.LocalDate

internal class FuzzyDateMapperTest : WordSpec({
    "a null LocalDate" should {
        val date: LocalDate? = null

        "be mapped to a FuzzyDate with all values Absent" {
            date.toFuzzyDate() shouldBeEqualToComparingFields FuzzyDateInput(
                year = Optional.Absent,
                month = Optional.Absent,
                day = Optional.Absent,
            )
        }
    }

    "a nullable LocalDate with value" should {
        val date: LocalDate? = LocalDate.of(2022, 7, 20)

        "be mapped to a FuzzyDate with all values Present" {
            date.toFuzzyDate() shouldBeEqualToComparingFields FuzzyDateInput(
                year = Optional.Present(2022),
                month = Optional.Present(7),
                day = Optional.Present(20),
            )
        }
    }

    "a LocalDate" should {
        val date: LocalDate = LocalDate.of(2022, 7, 20)

        "be mapped to a FuzzyDate with all values Present" {
            date.toFuzzyDate() shouldBeEqualToComparingFields FuzzyDateInput(
                year = Optional.Present(2022),
                month = Optional.Present(7),
                day = Optional.Present(20),
            )
        }
    }

    "a FuzzyDate that may have null values" should {
        val dates = Arb.bind<FuzzyDateInput>(
            mapOf(
                Optional::class to Arb.choice(
                    Arb.constant(Optional.Present(Arb.int().next())),
                    Arb.constant(Optional.Absent),
                ),
            ),
        ).edgecases()

        "be mapped to a FuzzyDate with all values Present" {
            dates.forEach {
                if (it.day is Optional.Present && it.month is Optional.Present && it.year is Optional.Present) {
                    it.takeIfValid().shouldNotBeNull()
                } else {
                    it.takeIfValid().shouldBeNull()
                }
            }
        }
    }
},)
