package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.ui.COLLECTION_SIZE
import dev.alvr.katana.features.lists.ui.randomCollection
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.maps.shouldHaveSize

internal class MangaListItemMapperTest :
    FreeSpec({
        "a random collection of manga" {
            randomCollection<MediaEntry.Manga>().entryMap().shouldHaveSize(COLLECTION_SIZE * COLLECTION_SIZE)
        }
    })
