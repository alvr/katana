package dev.alvr.katana.domain.token.usecases

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

internal class GetAnilistTokenUseCaseTest : FunSpec({
    val repo = mockk<TokenPreferencesRepository>()
    val useCase = GetAnilistTokenUseCase(repo)

    val token = Arb.bind<AnilistToken>().next()

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
},)
