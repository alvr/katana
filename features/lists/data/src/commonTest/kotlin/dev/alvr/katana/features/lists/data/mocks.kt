package dev.alvr.katana.features.lists.data

import com.apollographql.apollo.api.Error
import dev.alvr.katana.common.media.domain.models.ItemEntryId
import dev.alvr.katana.common.media.domain.models.lists.MediaList
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.positiveDouble
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.kotlinx.datetime.date
import io.kotest.property.kotlinx.datetime.datetime

internal val mediaListMock =
    MediaList(
        id = ItemEntryId(Arb.int().next()),
        score = Arb.positiveDouble().next(),
        progress = Arb.positiveInt().next(),
        progressVolumes = Arb.positiveInt().orNull().next(),
        repeat = Arb.positiveInt().next(),
        private = Arb.boolean().next(),
        notes = Arb.string().next(),
        hiddenFromStatusLists = Arb.boolean().next(),
        startedAt = Arb.date().orNull().next(),
        completedAt = Arb.date().orNull().next(),
        updatedAt = Arb.datetime().orNull().next(),
    )

internal val apolloErrorMock = Error.Builder(Arb.string().next()).build()
