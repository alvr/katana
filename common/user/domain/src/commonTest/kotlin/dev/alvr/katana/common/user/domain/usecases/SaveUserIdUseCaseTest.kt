package dev.alvr.katana.common.user.domain.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.repositories.UserRepository
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

internal class SaveUserIdUseCaseTest : FreeSpec(), KoinTest {
    private val dispatcher by inject<KatanaDispatcher>()
    private val repo = mock<UserRepository>()

    private lateinit var useCase: SaveUserIdUseCase

    init {
        "successfully saving user id" {
            everySuspend { repo.saveUserId() } returns Unit.right()
            useCase().shouldBeRight(Unit)
            verifySuspend { repo.saveUserId() }
        }

        listOf(
            UserFailure.FetchingUser to UserFailure.FetchingUser.left(),
            UserFailure.SavingUser to UserFailure.SavingUser.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure saving user id ($expected)" {
                everySuspend { repo.saveUserId() } returns failure
                useCase().shouldBeLeft(expected)
                verifySuspend { repo.saveUserId() }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = SaveUserIdUseCase(dispatcher, repo)
    }

    override fun extensions() = listOf(koinExtension(coreTestsModule))
}
