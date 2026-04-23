package dev.alvr.katana.common.user.data.managers

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.user.data.di.createUserDataTestGraph
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase

internal class UserIdManagerTest : FreeSpec() {
    private val repo = mock<UserRepository>()

    private lateinit var manager: UserIdManager

    init {
        "server return viewer is valid" {
            everySuspend { repo.getUserId() } returns UserId(37_384).right()
            manager.getId().shouldBeRight(37_384)
            verifySuspend { repo.getUserId() }
        }

        "server fails to return something" {
            everySuspend { repo.getUserId() } returns UserFailure.GettingUserId.left()
            manager.getId().shouldBeLeft(UserFailure.GettingUserId)
            verifySuspend { repo.getUserId() }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        manager = createUserDataTestGraph(repo).userIdManager
    }
}
