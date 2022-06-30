package dev.alvr.katana.ui.main

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.navigation.NavGraphs
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.mockk
import org.orbitmvi.orbit.test

internal class MainViewModelTest : BehaviorSpec({
    given("the MainViewModel") {
        val getAnilistTokenUseCase = mockk<GetAnilistTokenUseCase>()

        and("the user is logged in") {
            coEvery { getAnilistTokenUseCase.sync() } returns Arb.bind<AnilistToken>().next().some()
            val vm = MainViewModel(getAnilistTokenUseCase).test()

            `when`("the user does have a saved token") {
                vm.runOnCreate()

                then("the initial navGraph should be `NavGraphs.home`") {
                    vm.assert(MainState(initialNavGraph = NavGraphs.home)) {
                        states(
                            { copy(initialNavGraph = NavGraphs.home) },
                        )
                    }
                }
            }
        }

        and("the user is logged out") {
            coEvery { getAnilistTokenUseCase.sync() } returns none()
            val vm = MainViewModel(getAnilistTokenUseCase).test()

            `when`("the user does not have a saved token") {
                vm.runOnCreate()

                then("the initial navGraph should be `LoginNavGraph`") {
                    vm.assert(MainState(initialNavGraph = LoginNavGraph)) {
                        states(
                            { copy(initialNavGraph = LoginNavGraph) },
                        )
                    }
                }
            }
        }
    }
},)
