package dev.alvr.katana.common.user.domain.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.common.user.domain.userIdMock
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.di.coreTestsModule
import dev.alvr.katana.core.tests.koinExtension
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import org.koin.test.KoinTest
import org.koin.test.inject

internal class GetUserIdUseCaseTest : FreeSpec(), KoinTest {
    private val dispatcher by inject<KatanaDispatcher>()
    private val repo = mock<UserRepository>()

    private lateinit var useCase: GetUserIdUseCase

    init {
        "successfully getting user id" {
            everySuspend { repo.getUserId() } returns userIdMock.right()
            useCase().shouldBeRight(userIdMock)
            verifySuspend { repo.getUserId() }
        }

        listOf(
            UserFailure.GettingUserId to UserFailure.GettingUserId.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure getting user id ($expected)" {
                everySuspend { repo.getUserId() } returns failure
                useCase().shouldBeLeft(expected)
                verifySuspend { repo.getUserId() }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = GetUserIdUseCase(dispatcher, repo)
    }

    override fun extensions() = listOf(koinExtension(coreTestsModule))
}
