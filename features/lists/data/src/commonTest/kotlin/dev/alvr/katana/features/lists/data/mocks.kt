package dev.alvr.katana.features.lists.data

import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.Optional
import dev.alvr.katana.core.remote.type.FuzzyDateInput
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.positiveDouble
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import korlibs.time.Date
import korlibs.time.DateTimeTz

internal val mediaListCollectionQueryMock = MediaListCollectionQuery(
    user = Arb.positiveInt().orNull().next(),
    type = Arb.enum<MediaType>().next(),
)

internal val mediaListEntriesMutationMock = MediaListEntriesMutation(
    id = Arb.int().next(),
    score = Arb.positiveDouble().orNull().next(),
    progress = Arb.positiveInt().orNull().next(),
    progressVolumes = Arb.positiveInt().orNull().next(),
    repeat = Arb.positiveInt().orNull().next(),
    private = Arb.boolean().orNull().next(),
    notes = Arb.string().orNull().next(),
    hiddenFromStatusLists = Arb.boolean().orNull().next(),
    startedAt = Arb.fuzzyDate().orNull().next(),
    completedAt = Arb.fuzzyDate().orNull().next(),
)

internal val mediaListMock = MediaList(
    id = Arb.int().next(),
    score = Arb.positiveDouble().next(),
    progress = Arb.positiveInt().next(),
    progressVolumes = Arb.positiveInt().orNull().next(),
    repeat = Arb.positiveInt().next(),
    private = Arb.boolean().next(),
    notes = Arb.string().next(),
    hiddenFromStatusLists = Arb.boolean().next(),
    startedAt = Arb.date().orNull().next(),
    completedAt = Arb.date().orNull().next(),
    updatedAt = Arb.dateTimeTz().orNull().next(),
)

internal val apolloErrorMock = Error.Builder(Arb.string().next()).build()

private fun Arb.Companion.date() = arbitrary {
    Date(
        year = Arb.int(1970, 2030).next(),
        month = Arb.int(1, 12).next(),
        day = Arb.int(1, 28).next(),
    )
}

private fun Arb.Companion.dateTimeTz() = arbitrary {
    DateTimeTz.nowLocal()
}

private fun Arb.Companion.fuzzyDate() = arbitrary {
    FuzzyDateInput(
        year = Optional.presentIfNotNull(Arb.int().orNull().next()),
        month = Optional.presentIfNotNull(Arb.int().orNull().next()),
        day = Optional.presentIfNotNull(Arb.int().orNull().next()),
    )
}
