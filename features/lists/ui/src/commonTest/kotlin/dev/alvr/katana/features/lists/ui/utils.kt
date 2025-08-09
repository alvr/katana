package dev.alvr.katana.features.lists.ui

import dev.alvr.katana.features.lists.domain.models.ItemEntryId
import dev.alvr.katana.features.lists.domain.models.ItemMediaId
import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.domain.models.lists.MediaListEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string
import io.kotest.property.kotlinx.datetime.date
import io.kotest.property.kotlinx.datetime.datetime

internal inline fun <reified T : MediaEntry> randomCollection(): List<MediaListGroup<T>> = buildList {
    repeat(COLLECTION_SIZE) {
        add(
            MediaListGroup(
                name = Arb.string().next(),
                entries = buildList {
                    repeat(COLLECTION_SIZE) {
                        add(
                            MediaListEntry(
                                list = mediaListArb.next(),
                                entry = entryArb<T>().next(),
                            ),
                        )
                    }
                },
            ),
        )
    }
}

private val mediaListArb = arbitrary {
    MediaList(
        id = ItemEntryId(Arb.int().next()),
        score = 0.0,
        progress = Arb.int().next(),
        progressVolumes = null,
        repeat = Arb.int().next(),
        private = Arb.boolean().next(),
        notes = Arb.string().next(),
        hiddenFromStatusLists = Arb.boolean().next(),
        startedAt = Arb.date().orNull().next(),
        completedAt = Arb.date().orNull().next(),
        updatedAt = Arb.datetime().orNull().next(),
    )
}

private val commonMediaEntryArb = arbitrary {
    CommonMediaEntry(
        id = ItemMediaId(Arb.int().next()),
        title = Arb.string(minSize = 10).next(),
        coverImage = Arb.string().next(),
        format = Arb.enum<CommonMediaEntry.Format>().next(),
    )
}

@Suppress("UNCHECKED_CAST")
private inline fun <reified T : MediaEntry> entryArb(): Arb<T> = arbitrary {
    when (T::class) {
        MediaEntry.Anime::class -> MediaEntry.Anime(
            entry = commonMediaEntryArb.next(),
            episodes = null,
            nextEpisode = null,
        )

        MediaEntry.Manga::class -> MediaEntry.Manga(
            entry = commonMediaEntryArb.next(),
            chapters = null,
            volumes = null,
        )

        else -> error("")
    }
} as Arb<T>

internal const val COLLECTION_SIZE = 8
