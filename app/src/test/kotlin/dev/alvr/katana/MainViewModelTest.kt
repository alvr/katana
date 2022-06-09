package dev.alvr.katana

import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.navigation.NavGraphs
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.mockk

internal class MainViewModelTest : BehaviorSpec({
    given("the MainViewModel") {
        val getAnilistTokenUseCase = mockk<GetAnilistTokenUseCase>()
        val vm = MainViewModel(getAnilistTokenUseCase)

        `when`("the user does not have a saved token") {
            coEvery { getAnilistTokenUseCase.sync() } returns null

            then("the initial navGraph should be `LoginNavGraph`") {
                vm.initialNavGraph shouldBe LoginNavGraph
            }
        }

        `when`("the user does have a saved token") {
            coEvery { getAnilistTokenUseCase.sync() } returns Arb.bind<AnilistToken>().next()

            then("the initial navGraph should be `NavGraphs.home`") {
                vm.initialNavGraph shouldBe NavGraphs.home
            }
        }
    }
},)
