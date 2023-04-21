package dev.alvr.katana.data.remote.lists.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.FuzzyDateInput
import dev.alvr.katana.data.remote.lists.MediaListEntriesMutation
import dev.alvr.katana.domain.lists.models.lists.MediaList
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.property.Arb
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class MediaListMapperTest : TestBase() {
    @Test
    @DisplayName("WHEN a MediaList with all data non-null THEN should have all values present")
    fun `a MediaList with all data non-null`() = runTest {
        // GIVEN
        val mediaList = MediaList(
            id = Int.zero,
            score = Double.zero,
            progress = Int.zero,
            progressVolumes = Int.zero,
            repeat = Int.zero,
            private = false,
            notes = String.empty,
            hiddenFromStatusLists = false,
            startedAt = LocalDate.of(2022, 7, 20),
            completedAt = LocalDate.of(2022, 7, 20),
            updatedAt = Arb.localDateTime().next(),
        )

        // WHEN
        val result = mediaList.toMutation()

        // THEN
        result shouldBeEqualToComparingFields MediaListEntriesMutation(
            id = Int.zero,
            score = Optional.Present(Double.zero),
            progress = Optional.Present(Int.zero),
            progressVolumes = Optional.Present(Int.zero),
            repeat = Optional.Present(Int.zero),
            private = Optional.Present(false),
            notes = Optional.Present(String.empty),
            hiddenFromStatusLists = Optional.Present(false),
            startedAt = Optional.Present(
                FuzzyDateInput(
                    year = Optional.Present(2022),
                    month = Optional.Present(7),
                    day = Optional.Present(20),
                ),
            ),
            completedAt = Optional.Present(
                FuzzyDateInput(
                    year = Optional.Present(2022),
                    month = Optional.Present(7),
                    day = Optional.Present(20),
                ),
            ),
        )
    }

    @Test
    @DisplayName("WHEN a MediaList with null data THEN should have all values present")
    fun `a MediaList with null data`() = runTest {
        // GIVEN
        val mediaList = MediaList(
            id = Int.zero,
            score = Double.zero,
            progress = Int.zero,
            progressVolumes = null,
            repeat = Int.zero,
            private = false,
            notes = String.empty,
            hiddenFromStatusLists = false,
            startedAt = null,
            completedAt = null,
            updatedAt = null,
        )

        // WHEN
        val result = mediaList.toMutation()

        // THEN
        result shouldBeEqualToComparingFields MediaListEntriesMutation(
            id = Int.zero,
            score = Optional.Present(Double.zero),
            progress = Optional.Present(Int.zero),
            progressVolumes = Optional.Absent,
            repeat = Optional.Present(Int.zero),
            private = Optional.Present(false),
            notes = Optional.Present(String.empty),
            hiddenFromStatusLists = Optional.Present(false),
            startedAt = Optional.Absent,
            completedAt = Optional.Absent,
        )
    }
}
