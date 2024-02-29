package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.ui.lists.COLLECTION_SIZE
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.randomCollection
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldBeTypeOf

internal class AnimeListItemMapperTest : FreeSpec({
    "a random collection of anime" {
        randomCollection<MediaEntry.Anime>()
            .toMediaItems()
            .shouldHaveSize(COLLECTION_SIZE * COLLECTION_SIZE)
            .first().shouldBeTypeOf<MediaListItem.AnimeListItem>()
    }
})
