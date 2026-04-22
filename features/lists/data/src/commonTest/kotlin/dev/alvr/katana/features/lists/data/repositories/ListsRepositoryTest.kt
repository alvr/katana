package dev.alvr.katana.features.lists.data.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.lists.data.di.createListsRepositoryTestGraph
import dev.alvr.katana.features.lists.data.mediaListMock
import dev.alvr.katana.features.lists.data.sources.ListsRemoteSource
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
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.flowOf

internal class ListsRepositoryTest : FreeSpec() {
    private val remoteSource = mock<ListsRemoteSource>()

    private lateinit var repo: ListsRepository

    init {
        "collections" -
            {
                val animeCollection = MediaCollection<MediaEntry.Anime>(emptyList())
                val mangaCollection = MediaCollection<MediaEntry.Manga>(emptyList())
                every { remoteSource.animeCollection } returns flowOf(animeCollection.right())
                every { remoteSource.mangaCollection } returns flowOf(mangaCollection.right())

                "collecting anime collection flow" {
                    repo.animeCollection.test {
                        awaitItem().shouldBeRight(animeCollection)
                        awaitComplete()
                    }

                    verify { remoteSource.animeCollection }
                }

                "collecting manga collection flow" {
                    repo.mangaCollection.test {
                        awaitItem().shouldBeRight(mangaCollection)
                        awaitComplete()
                    }

                    verify { remoteSource.mangaCollection }
                }
            }

        "successfully updating list" {
            everySuspend { remoteSource.updateList(any()) } returns Unit.right()
            repo.updateList(mediaListMock).shouldBeRight(Unit)
            verifySuspend { remoteSource.updateList(any()) }
        }

        listOf(ListsFailure.UpdatingList to ListsFailure.UpdatingList.left(), Failure.Unknown to Failure.Unknown.left())
            .forEach { (expected, failure) ->
                "failure updating the list ($expected)" {
                    everySuspend { remoteSource.updateList(any()) } returns failure
                    repo.updateList(mediaListMock).shouldBeLeft(expected)
                    verifySuspend { remoteSource.updateList(mediaListMock) }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        repo = createListsRepositoryTestGraph(remoteSource).listsRepository
    }
}
