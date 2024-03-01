package dev.alvr.katana.features.lists.data.mappers.requests

import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.remote.type.FuzzyDateInput
import dev.alvr.katana.features.lists.data.MediaListEntriesMutation
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import korlibs.time.Date
import korlibs.time.DateTimeTz

internal class MediaListMapperTest : FreeSpec({
    "a MediaList with all values present" {
        MediaList(
            id = Int.zero,
            score = Double.zero,
            progress = Int.zero,
            progressVolumes = Int.zero,
            repeat = Int.zero,
            private = false,
            notes = String.empty,
            hiddenFromStatusLists = false,
            startedAt = Date(2022, 7, 20),
            completedAt = Date(2022, 7, 20),
            updatedAt = DateTimeTz.nowLocal(),
        ).toMutation() shouldBeEqual MediaListEntriesMutation(
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

    "a MediaList with nullable values" {
        MediaList(
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
        ).toMutation() shouldBeEqual MediaListEntriesMutation(
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
})
