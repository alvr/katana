package dev.alvr.katana.features.lists.ui

import dev.alvr.katana.common.media.domain.models.ItemEntryId
import dev.alvr.katana.common.media.domain.models.ItemMediaId
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.common.media.domain.models.lists.MediaListEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListGroup
import dev.alvr.katana.core.common.zero
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
                entries =
                    buildList {
                        repeat(COLLECTION_SIZE) {
                            add(MediaListEntry(list = mediaListArb.next(), entry = entryArb<T>().next()))
                        }
                    },
            )
        )
    }
}

private val mediaListArb = arbitrary {
    MediaList(
        id = ItemEntryId(Arb.int().bind()),
        score = Float.zero,
        progress = Arb.int().bind(),
        progressVolumes = null,
        repeat = Arb.int().bind(),
        private = Arb.boolean().bind(),
        notes = Arb.string().bind(),
        hiddenFromStatusLists = Arb.boolean().bind(),
        startedAt = Arb.date().orNull().bind(),
        completedAt = Arb.date().orNull().bind(),
        updatedAt = Arb.datetime().orNull().bind(),
    )
}

private val commonMediaEntryArb = arbitrary {
    CommonMediaEntry(
        id = ItemMediaId(Arb.int().bind()),
        title = Arb.string(minSize = 10).bind(),
        coverImage = Arb.string().bind(),
        format = Arb.enum<CommonMediaEntry.Format>().bind(),
    )
}

@Suppress("UNCHECKED_CAST")
private inline fun <reified T : MediaEntry> entryArb(): Arb<T> =
    arbitrary {
        when (T::class) {
            MediaEntry.Anime::class ->
                MediaEntry.Anime(entry = commonMediaEntryArb.bind(), episodes = null, nextEpisode = null)

            MediaEntry.Manga::class ->
                MediaEntry.Manga(entry = commonMediaEntryArb.bind(), chapters = null, volumes = null)

            else -> error("")
        }
    }
        as Arb<T>

internal const val COLLECTION_SIZE = 8
