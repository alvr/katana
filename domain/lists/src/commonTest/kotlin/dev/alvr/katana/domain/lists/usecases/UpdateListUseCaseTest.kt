package dev.alvr.katana.domain.lists.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.fakeMediaList
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import dev.alvr.katana.domain.lists.repositories.MockListsRepository
import io.kotest.core.spec.style.FreeSpec
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks

@UsesFakes(MediaList::class)
@UsesMocks(ListsRepository::class)
internal class UpdateListUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockListsRepository(mocker)

    private val useCase = UpdateListUseCase(repo)

    init {
        "successfully updating the list" {
            mocker.everySuspending { repo.updateList(isAny()) } returns Unit.right()
            useCase(fakeMediaList()).shouldBeRight(Unit)
            mocker.verifyWithSuspend { repo.updateList(fakeMediaList()) }
        }

        listOf(
            ListsFailure.UpdatingList to ListsFailure.UpdatingList.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure updating the list ($expected)" {
                mocker.everySuspending { repo.updateList(isAny()) } returns failure
                useCase(fakeMediaList()).shouldBeLeft(expected)
                mocker.verifyWithSuspend { repo.updateList(fakeMediaList()) }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
