package dev.alvr.katana.ui.main.viewmodel

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.domain.session.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import dev.alvr.katana.ui.main.navigation.NavGraphs
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.test

internal class MainViewModelTest : BehaviorSpec() {
    init {
        given("the MainViewModel") {
            val clearActiveSession = mockk<ClearActiveSessionUseCase>()
            val getAnilistToken = mockk<GetAnilistTokenUseCase>()
            val observeSession = mockk<ObserveActiveSessionUseCase>()

            and("the user is logged in") {
                justRun { observeSession() }

                every { getAnilistToken.sync() } returns valueMockk<AnilistToken>().some()
                val vm = MainViewModel(clearActiveSession, getAnilistToken, observeSession).test()

                `when`("the user does have a saved token") {
                    every { observeSession.flow } returns emptyFlow()

                    vm.runOnCreate()

                    then("the initial navGraph should be `NavGraphs.home`") {
                        vm.assert(MainState(initialNavGraph = NavGraphs.home)) {
                            states(
                                { copy(initialNavGraph = NavGraphs.home) },
                            )
                        }
                    }
                }

                and("has an active session") {
                    every { observeSession.flow } returns flowOf(true, true, false)

                    `when`("the session expires") {
                        vm.runOnCreate()

                        then("the initial navGraph should be `NavGraphs.home`") {
                            vm.assert(MainState(initialNavGraph = NavGraphs.home)) {
                                states(
                                    { copy(initialNavGraph = NavGraphs.home) },
                                    { copy(isSessionActive = false) },
                                )
                            }
                        }
                    }

                    and("the session expires") {
                        vm.runOnCreate()

                        `when`("the user clear the active session") {
                            coJustRun { clearActiveSession() }
                            vm.testIntent { clearSession() }

                            coVerify(exactly = 1) { clearActiveSession() }
                        }
                    }
                }
            }

            and("the user is logged out") {
                justRun { observeSession() }
                every { observeSession.flow } returns emptyFlow()

                every { getAnilistToken.sync() } returns none()
                val vm = MainViewModel(clearActiveSession, getAnilistToken, observeSession).test()

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
    }
}
