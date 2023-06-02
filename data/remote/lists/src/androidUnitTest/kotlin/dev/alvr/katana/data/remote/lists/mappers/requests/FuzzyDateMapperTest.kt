package dev.alvr.katana.data.remote.lists.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.FuzzyDateInput
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
import korlibs.time.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class FuzzyDateMapperTest : TestBase() {
    @Test
    @DisplayName("WHEN a null LocalDate THEN it should be mapped to a FuzzyDate with all values Absent")
    fun `a null LocalDate`() = runTest {
        // GIVEN
        val date: Date? = null

        // WHEN
        val result = date.toFuzzyDate()

        // THEN
        result shouldBeEqualToComparingFields FuzzyDateInput(
            year = Optional.Absent,
            month = Optional.Absent,
            day = Optional.Absent,
        )
    }

    @Test
    @DisplayName("WHEN a nullable LocalDate with value THEN it should be mapped to a FuzzyDate with all values Present")
    fun `a nullable LocalDate with value`() = runTest {
        // GIVEN
        val date: Date? = Date(2022, 7, 20)

        // WHEN
        val result = date.toFuzzyDate()

        // THEN
        result shouldBeEqualToComparingFields FuzzyDateInput(
            year = Optional.Present(2022),
            month = Optional.Present(7),
            day = Optional.Present(20),
        )
    }

    @Test
    @DisplayName("WHEN a LocalDate THEN it should be mapped to a FuzzyDate with all values Present")
    fun `a LocalDate`() = runTest {
        // GIVEN
        val date: Date = Date(2022, 7, 20)

        // WHEN
        val result = date.toFuzzyDate()

        // THEN
        result shouldBeEqualToComparingFields FuzzyDateInput(
            year = Optional.Present(2022),
            month = Optional.Present(7),
            day = Optional.Present(20),
        )
    }

    @Test
    @DisplayName(
        """
        WHEN a FuzzyDate that may have null values
        THEN it should be mapped to a FuzzyDate with all values Present
        """,
    )
    fun `a FuzzyDate that may have null values`() = runTest {
        // GIVEN
        val dates = Arb.bind<FuzzyDateInput>(
            mapOf(
                Optional::class to Arb.choice(
                    Arb.constant(Optional.Present(Arb.int().next())),
                    Arb.constant(Optional.Absent),
                ),
            ),
        ).edgecases()

        // THEN
        dates.forEach {
            if (it.day is Optional.Present && it.month is Optional.Present && it.year is Optional.Present) {
                it.takeIfValid().shouldNotBeNull()
            } else {
                it.takeIfValid().shouldBeNull()
            }
        }
    }
}
