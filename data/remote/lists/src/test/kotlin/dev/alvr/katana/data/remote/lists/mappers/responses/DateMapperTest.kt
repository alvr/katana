package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.common.tests.TestBase
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldBeNull
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExperimentalCoroutinesApi
internal class DateMapperTest : TestBase() {
    @ParameterizedTest(name = "WHEN a date has a null value THEN the LocalDate should be null")
    @MethodSource("nullValueArgs")
    fun `with a null value`(date: LocalDate?) = runTest {
        date.shouldBeNull()
    }

    @Test
    @DisplayName("WHEN a date does not have a null value THEN the LocalDate should be the same")
    fun `without a null value`() = runTest {
        dateMapper(13, 4, 2022)?.shouldBeEqualComparingTo(LocalDate.of(2022, 4, 13))
    }

    private companion object {
        @JvmStatic
        fun nullValueArgs() = listOf(
            Arguments.of(dateMapper(null, null, null)),
            Arguments.of(dateMapper(13, null, null)),
            Arguments.of(dateMapper(null, 4, null)),
            Arguments.of(dateMapper(null, null, 2022)),
            Arguments.of(dateMapper(13, 4, null)),
            Arguments.of(dateMapper(13, null, 2022)),
            Arguments.of(dateMapper(null, 4, 2022)),
        )
    }
}
