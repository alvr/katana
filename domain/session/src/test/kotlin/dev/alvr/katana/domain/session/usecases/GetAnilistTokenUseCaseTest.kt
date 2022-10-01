package dev.alvr.katana.domain.session.usecases

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify

internal class GetAnilistTokenUseCaseTest : FunSpec() {
    private val repo = mockk<SessionRepository>()
    private val useCase = spyk(GetAnilistTokenUseCase(repo))

    private val token = valueMockk<AnilistToken>()

    init {
        context("successful getting") {
            coEvery { repo.getAnilistToken() } returns token.some()

            test("invoke should be some token") {
                useCase().shouldBeSome(token)
                coVerify(exactly = 1) { repo.getAnilistToken() }
            }

            test("sync should be some token") {
                useCase.sync().shouldBeSome(token)
                coVerify(exactly = 1) { repo.getAnilistToken() }
            }
        }

        context("failure getting") {
            coEvery { repo.getAnilistToken() } returns none()

            test("invoke should return None") {
                useCase().shouldBeNone()
                coVerify(exactly = 1) { repo.getAnilistToken() }
            }

            test("sync should return None") {
                useCase.sync().shouldBeNone()
                coVerify(exactly = 1) { repo.getAnilistToken() }
            }
        }

        test("invoke the use case should call the invoke operator") {
            coEvery { repo.getAnilistToken() } returns mockk()

            useCase()

            coVerify(exactly = 1) { useCase.invoke(Unit) }
            coVerify(exactly = 1) { repo.getAnilistToken() }
        }

        test("sync the use case should call the invoke operator") {
            coEvery { repo.getAnilistToken() } returns mockk()

            useCase.sync()

            verify(exactly = 1) { useCase.sync(Unit) }
            coVerify(exactly = 1) { repo.getAnilistToken() }
        }
    }
}
