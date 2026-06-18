package dev.alvr.katana.features.lists.data.repositories

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.lists.data.di.createListsRepositoryTestGraph
import dev.alvr.katana.features.lists.data.mediaListMock
import dev.alvr.katana.features.lists.data.sources.ListsRemoteSource
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase

internal class ListsRepositoryTest : FreeSpec() {
    private val remoteSource = mock<ListsRemoteSource>()

    private lateinit var repo: ListsRepository

    init {
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
