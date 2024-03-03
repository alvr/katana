package dev.alvr.katana.features.lists.domain.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.mediaListMock
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec

internal class UpdateListUseCaseTest : FreeSpec() {
    private val repo = mock<ListsRepository>()

    private val useCase = UpdateListUseCase(repo)

    init {
        "successfully updating the list" {
            everySuspend { repo.updateList(any()) } returns Unit.right()
            useCase(mediaListMock).shouldBeRight(Unit)
            verifySuspend { repo.updateList(mediaListMock) }
        }

        listOf(
            ListsFailure.UpdatingList to ListsFailure.UpdatingList.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure updating the list ($expected)" {
                everySuspend { repo.updateList(any()) } returns failure
                useCase(mediaListMock).shouldBeLeft(expected)
                verifySuspend { repo.updateList(mediaListMock) }
            }
        }
    }
}
