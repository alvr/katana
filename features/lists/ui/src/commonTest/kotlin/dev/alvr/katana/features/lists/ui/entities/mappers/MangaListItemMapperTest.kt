package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.ui.COLLECTION_SIZE
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.randomCollection
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldBeTypeOf

internal class MangaListItemMapperTest : FreeSpec({
    "a random collection of manga" {
        randomCollection<MediaEntry.Manga>()
            .toMediaItems()
            .shouldHaveSize(COLLECTION_SIZE * COLLECTION_SIZE)
            .first().shouldBeTypeOf<MediaListItem.MangaListItem>()
    }
})
