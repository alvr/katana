package dev.alvr.katana.data.preferences.token.managers

import arrow.core.None
import arrow.core.some
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.managers.GetTokenBearerManager
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.mockk

internal class GetTokenBearerManagerTest : DescribeSpec({
    describe("a token bearer manager") {
        val getAnilistToken = mockk<GetAnilistTokenUseCase>()
        val manager: GetTokenBearerManager = GetTokenBearerManagerImpl(getAnilistToken)

        describe("if there is some token") {
            val token = Arb.bind<AnilistToken>().next()
            coEvery { getAnilistToken.sync() } returns token.some()

            it("the manager should return the same token") {
                manager.token shouldBe token.token
            }
        }

        describe("if there is no token") {
            coEvery { getAnilistToken.sync() } returns None

            it("the manager should return null") {
                manager.token.shouldBeNull()
            }
        }
    }
},)
