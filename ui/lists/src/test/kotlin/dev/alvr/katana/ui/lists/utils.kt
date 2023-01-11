package dev.alvr.katana.ui.lists

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import java.time.LocalDate
import java.time.LocalDateTime

internal inline fun <reified T : MediaEntry> randomCollection() = buildList {
    repeat(COLLECTION_SIZE) {
        add(
            MediaListGroup(
                name = Arb.string().next(),
                entries = buildList {
                    repeat(COLLECTION_SIZE) {
                        add(
                            MediaListEntry(
                                list = Arb.bind<MediaList>(
                                    mapOf(
                                        LocalDate::class to Arb.localDate(),
                                        LocalDateTime::class to Arb.localDateTime(),
                                    ),
                                ).next(),
                                entry = Arb.bind<T>().next(),
                            ),
                        )
                    }
                },
            ),
        )
    }
}

internal const val COLLECTION_SIZE = 8
