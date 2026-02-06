package dev.alvr.katana.features.lists.domain

import dev.alvr.katana.features.lists.domain.models.ItemEntryId
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.kotest.property.kotlinx.datetime.date
import io.kotest.property.kotlinx.datetime.datetime

internal val mediaListMock =
    MediaList(
        id = ItemEntryId(Arb.int().next()),
        score = Arb.double().next(),
        progress = Arb.int().next(),
        progressVolumes = Arb.int().next(),
        repeat = Arb.int().next(),
        private = Arb.boolean().next(),
        notes = Arb.string().next(),
        hiddenFromStatusLists = Arb.boolean().next(),
        startedAt = Arb.date().next(),
        completedAt = Arb.date().next(),
        updatedAt = Arb.datetime().next(),
    )
