package dev.alvr.katana.features.home.ui.viewmodel

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.coEitherJustRun
import dev.alvr.katana.core.tests.orbitTestScope
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

internal class HomeViewModelTest : BehaviorSpec() {
    private val clearActiveSession = mockk<ClearActiveSessionUseCase>()
    private val observeActiveSession = mockk<ObserveActiveSessionUseCase>()

    private lateinit var viewModel: HomeViewModel

    init {
        given("a logged in user") {
            `when`("the user does have a saved token") {
                then("the initial navGraph should be `KatanaScreen.Home`") {
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
                    then("the initial navGraph should be `KatanaScreen.Home`") {
                        initMocks()

                        viewModel.test(orbitTestScope) {
                            runOnCreate()
                            expectInitialState()
                            expectSideEffect(HomeEffect.ExpiredToken)
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
                        expectSideEffect(HomeEffect.ExpiredToken)
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
                        expectSideEffect(HomeEffect.ExpiredToken)
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
                        expectSideEffect(HomeEffect.ExpiredToken)
                    }

                    verifyMocks()
                }
            }
        }
    }

    override suspend fun beforeTest(testCase: TestCase) {
        clearMocks(clearActiveSession, observeActiveSession)

        viewModel = HomeViewModel(observeActiveSession, clearActiveSession)
    }

    private fun initMocks(
        sessionFlow: Flow<Either<Failure, Boolean>> = flowOf(
            true.right(),
            true.right(),
            false.right(),
        )
    ) {
        justRun { observeActiveSession() }
        every { observeActiveSession.flow } returns sessionFlow
    }

    private fun verifyMocks() {
        verify(exactly = 1) { observeActiveSession() }
        verify(exactly = 1) { observeActiveSession.flow }
    }

    private companion object {
        val LOGGED_OUT_TEST = NamedTag("loggedOut")
    }
}
