package dev.alvr.katana.domain.lists

import dev.alvr.katana.domain.lists.models.lists.MediaList
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import korlibs.time.Date
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.TimezoneOffset

internal val mediaListMock = MediaList(
    id = Arb.int().next(),
    score = Arb.double().next(),
    progress = Arb.int().next(),
    progressVolumes = Arb.int().next(),
    repeat = Arb.int().next(),
    private = Arb.boolean().next(),
    notes = Arb.string().next(),
    hiddenFromStatusLists = Arb.boolean().next(),
    startedAt = Date(2022, 1, 1),
    completedAt = Date(2022, 12, 31),
    updatedAt = DateTimeTz.local(DateTime(2022, 12, 31, 23, 59, 59), TimezoneOffset.UTC),
)
