package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.ui.lists.viewmodel.animeListItem1
import dev.alvr.katana.ui.lists.viewmodel.animeListItem2
import dev.alvr.katana.ui.lists.viewmodel.mangaListItem1
import dev.alvr.katana.ui.lists.viewmodel.mangaListItem2
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class UserListMapperTest : TestBase() {
    @Test
    @DisplayName("GIVEN a collection of anime WHEN mapping it to UserList THEN should have the name and its count")
    fun `a collection of anime`() = runTest {
        // GIVEN
        val animeCollection = mapOf(
            "MyCustomAnimeList" to listOf(animeListItem1),
            "MyCustomAnimeList2" to listOf(animeListItem2),
        )

        // WHEN
        val result = animeCollection.toUserList()

        // THEN
        result
            .shouldHaveSize(animeCollection.size)
            .forAll { (name, count) ->
                name shouldBeIn animeCollection.keys
                count shouldBe 1
            }
    }

    @Test
    @DisplayName("GIVEN a collection of manga WHEN mapping it to UserList THEN should have the name and its count")
    fun `a collection of manga`() = runTest {
        // GIVEN
        val mangaCollection = mapOf(
            "MyCustomMangaList" to listOf(mangaListItem1),
            "MyCustomMangaList2" to listOf(mangaListItem2),
        )

        // WHEN
        val result = mangaCollection.toUserList()

        // THEN
        result
            .shouldHaveSize(mangaCollection.size)
            .forAll { (name, count) ->
                name shouldBeIn mangaCollection.keys
                count shouldBe 1
            }
    }
}
