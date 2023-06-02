package dev.alvr.katana.ui.lists

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveLong
import io.kotest.property.arbitrary.string
import korlibs.time.DateTimeTz

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
                                        DateTimeTz::class to arbitrary {
                                            DateTimeTz.fromUnix(Arb.positiveLong().next())
                                        },
                                    ),
                                ).next(),
                                entry = Arb.bind<T>(
                                    mapOf(
                                        DateTimeTz::class to arbitrary {
                                            DateTimeTz.fromUnix(Arb.positiveLong().next())
                                        },
                                    ),
                                ).next(),
                            ),
                        )
                    }
                },
            ),
        )
    }
}

internal const val COLLECTION_SIZE = 8
