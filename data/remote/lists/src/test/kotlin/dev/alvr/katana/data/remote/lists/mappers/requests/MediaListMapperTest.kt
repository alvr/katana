package dev.alvr.katana.data.remote.lists.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.data.remote.base.type.FuzzyDateInput
import dev.alvr.katana.data.remote.lists.MediaListEntriesMutation
import dev.alvr.katana.domain.lists.models.lists.MediaList
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.property.Arb
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import java.time.LocalDate

internal class MediaListMapperTest : WordSpec({
    "a MediaList with all data non-null" should {
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

        "have all values present" {
            mediaList.toMutation() shouldBeEqualToComparingFields MediaListEntriesMutation(
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
    }

    "a MediaList with null data" should {
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

        "have all values present" {
            mediaList.toMutation() shouldBeEqualToComparingFields MediaListEntriesMutation(
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
},)
