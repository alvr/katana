package dev.alvr.katana.features.lists.data.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.lists.data.mediaListMock
import dev.alvr.katana.features.lists.data.sources.CommonListsRemoteSource
import dev.alvr.katana.features.lists.data.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.features.lists.data.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import kotlinx.coroutines.flow.flowOf

internal class ListsRepositoryTest : FreeSpec() {
    private val commonSource = mock<CommonListsRemoteSource>()
    private val animeSource = mock<AnimeListsRemoteSource>()
    private val mangaSource = mock<MangaListsRemoteSource>()

    private val repo: ListsRepository = ListsRepositoryImpl(commonSource, animeSource, mangaSource)

    init {
        "collecting anime collection flow" {
            val collection = MediaCollection<MediaEntry.Anime>(emptyList())
            every { animeSource.animeCollection } returns flowOf(collection.right())

            repo.animeCollection.test {
                awaitItem().shouldBeRight(collection)
                awaitComplete()
            }

            verify { animeSource.animeCollection }
        }

        "collecting manga collection flow" {
            val collection = MediaCollection<MediaEntry.Manga>(emptyList())
            every { mangaSource.mangaCollection } returns flowOf(collection.right())

            repo.mangaCollection.test {
                awaitItem().shouldBeRight(collection)
                awaitComplete()
            }

            verify { mangaSource.mangaCollection }
        }

        "successfully updating list" {
            everySuspend { commonSource.updateList(any()) } returns Unit.right()
            repo.updateList(mediaListMock).shouldBeRight(Unit)
            verifySuspend { commonSource.updateList(any()) }
        }

        listOf(
            ListsFailure.UpdatingList to ListsFailure.UpdatingList.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure updating the list ($expected)" {
                everySuspend { commonSource.updateList(any()) } returns failure
                repo.updateList(mediaListMock).shouldBeLeft(expected)
                verifySuspend { commonSource.updateList(mediaListMock) }
            }
        }
    }
}
