package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.ui.lists.COLLECTION_SIZE
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.randomCollection
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class MangaListItemMapperTest : TestBase() {
    @Test
    @DisplayName("GIVEN a random collection of manga WHEN mapping it THEN should have the same size after mapping")
    fun `a random collection of manga`() = runTest {
        // GIVEN
        val mangaCollection = randomCollection<MediaEntry.Manga>()

        // WHEN
        val result = mangaCollection.toMediaItems()

        // THEN
        result
            .shouldHaveSize(COLLECTION_SIZE * COLLECTION_SIZE)
            .first().shouldBeTypeOf<MediaListItem.MangaListItem>()
    }
}
