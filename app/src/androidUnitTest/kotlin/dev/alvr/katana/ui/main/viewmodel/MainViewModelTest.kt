package dev.alvr.katana.ui.main.viewmodel

import arrow.core.Either
import arrow.core.left
import arrow.core.none
import arrow.core.right
import arrow.core.some
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.common.tests.orbitTestScope
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.domain.session.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import io.kotest.core.NamedTag
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.test.test

internal class MainViewModelTest : BehaviorSpec() {
    private val clearActiveSession = mockk<ClearActiveSessionUseCase>()
    private val getAnilistToken = mockk<GetAnilistTokenUseCase>()
    private val observeSession = mockk<ObserveActiveSessionUseCase>()

    private lateinit var viewModel: MainViewModel

    init {
        given("a logged in user") {
            `when`("the user does have a saved token") {
                then("the initial navGraph should be `NavGraphs.home`") {
                    initMocks(sessionFlow = emptyFlow())

                    viewModel.test(orbitTestScope) {
                        runOnCreate()
                        expectInitialState()
                    }

                    verifyMocks()
                }
            }

            `when`("the user has an active session") {
                and("the session expires") {
                    then("the initial navGraph should be `NavGraphs.home`") {
                        initMocks()

                        viewModel.test(orbitTestScope) {
                            runOnCreate()
                            expectInitialState()
                            expectState { copy(isSessionActive = false) }
                        }

                        verifyMocks()
                    }
                }
            }

            `when`("WHEN the user has an expired session") {
                then("the active session is cleared (success)") {
                    initMocks()
                    coEitherJustRun { clearActiveSession() }

                    viewModel.test(orbitTestScope) {
                        runOnCreate()
                        expectInitialState()
                        containerHost.clearSession()
                        expectState { copy(isSessionActive = false) }
                    }

                    coVerify(exactly = 1) { clearActiveSession() }
                    verifyMocks()
                }

                then("the active session is cleared (failure)") {
                    initMocks()
                    coEvery { clearActiveSession() } returns mockk<SessionFailure>().left()

                    viewModel.test(orbitTestScope) {
                        runOnCreate()
                        expectInitialState()
                        containerHost.clearSession()
                        expectState { copy(isSessionActive = false) }
                    }

                    coVerify(exactly = 1) { clearActiveSession() }
                    verifyMocks()
                }
            }

            `when`("something wrong happens when observing the session") {
                then("should update the session as not active") {
                    initMocks(sessionFlow = flowOf(mockk<SessionFailure>().left()))

                    viewModel.test(orbitTestScope) {
                        runOnCreate()
                        expectInitialState()
                        expectState { copy(isSessionActive = false) }
                    }

                    verifyMocks()
                }
            }
        }

        given("a logged out user") {
            `when`("the user does not have a saved token") {
                then("the initial navGraph should be `LoginNavGraph`").config(tags = setOf(LOGGED_OUT_TEST)) {
                    viewModel.test(orbitTestScope) {
                        expectState(MainState(initialNavGraph = LoginNavGraph))
                    }

                    verify(exactly = 1) { getAnilistToken.sync() }
                }
            }
        }
    }

    override suspend fun beforeTest(testCase: TestCase) {
        clearMocks(clearActiveSession, getAnilistToken, observeSession)

        every { getAnilistToken.sync() } answers {
            if (testCase.config.tags.contains(LOGGED_OUT_TEST)) {
                none()
            } else {
                AnilistToken("TOKEN").some()
            }
        }

        viewModel = MainViewModel(clearActiveSession, getAnilistToken, observeSession)
    }

    private fun initMocks(
        sessionFlow: Flow<Either<Failure, Boolean>> = flowOf(
            true.right(),
            true.right(),
            false.right(),
        )
    ) {
        justRun { observeSession() }
        every { observeSession.flow } returns sessionFlow
    }

    private fun verifyMocks() {
        verify(exactly = 1) { observeSession() }
        verify(exactly = 1) { observeSession.flow }
        verify(exactly = 1) { getAnilistToken.sync() }
    }

    private companion object {
        val LOGGED_OUT_TEST = NamedTag("loggedOut")
    }
}
