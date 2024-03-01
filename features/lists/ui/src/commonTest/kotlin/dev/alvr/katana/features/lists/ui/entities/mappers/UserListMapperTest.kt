package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.features.lists.ui.viewmodel.animeListItem1
import dev.alvr.katana.features.lists.ui.viewmodel.animeListItem2
import dev.alvr.katana.features.lists.ui.viewmodel.mangaListItem1
import dev.alvr.katana.features.lists.ui.viewmodel.mangaListItem2
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

internal class UserListMapperTest : FreeSpec({
    "a collection of anime" {
        mapOf(
            "MyCustomAnimeList" to listOf(animeListItem1),
            "MyCustomAnimeList2" to listOf(animeListItem2),
        ).also { collection ->
            collection.toUserList()
                .shouldHaveSize(collection.size)
                .forAll { (name, count) ->
                    name shouldBeIn collection.keys
                    count shouldBe 1
                }
        }
    }

    "a collection of manga" {
        mapOf(
            "MyCustomMangaList" to listOf(mangaListItem1),
            "MyCustomMangaList2" to listOf(mangaListItem2),
        ).also { collection ->
            collection.toUserList()
                .shouldHaveSize(collection.size)
                .forAll { (name, count) ->
                    name shouldBeIn collection.keys
                    count shouldBe 1
                }
        }
    }
})
