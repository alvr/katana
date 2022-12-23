package dev.alvr.katana.ui.main.viewmodel

import arrow.core.Either
import arrow.core.left
import arrow.core.none
import arrow.core.right
import arrow.core.some
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.domain.session.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.ui.login.navigation.LoginNavGraph
import dev.alvr.katana.ui.main.navigation.NavGraphs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.orbitmvi.orbit.SuspendingTestContainerHost
import org.orbitmvi.orbit.test

@ExperimentalCoroutinesApi
internal class MainViewModelTest : TestBase() {
    @MockK
    private lateinit var clearActiveSession: ClearActiveSessionUseCase
    @MockK
    private lateinit var getAnilistToken: GetAnilistTokenUseCase
    @MockK
    private lateinit var observeSession: ObserveActiveSessionUseCase

    private lateinit var viewModel: SuspendingTestContainerHost<MainState, Nothing, MainViewModel>

    @Nested
    @DisplayName("the user is logged in")
    inner class LoggedIn {
        @Test
        @DisplayName(
            """
            WHEN the user does have a saved token
            THEN the initial navGraph should be `NavGraphs.home`
            """,
        )
        fun `user does have a saved token`() = runTest {
            // GIVEN
            initMocks(sessionFlow = emptyFlow())

            // WHEN
            viewModel = MainViewModel(clearActiveSession, getAnilistToken, observeSession).test()
            viewModel.runOnCreate()

            // THEN
            viewModel.assert(MainState(initialNavGraph = NavGraphs.home))
            verifyMocks()
        }

        @Test
        @DisplayName(
            """
            WHEN the user has an active session AND the session expires
            THEN the initial navGraph should be `NavGraphs.home`
            """,
        )
        fun `the user has an active session AND the session expires`() = runTest {
            // GIVEN
            initMocks()

            // WHEN
            viewModel = MainViewModel(clearActiveSession, getAnilistToken, observeSession).test()
            viewModel.runOnCreate()

            // THEN
            viewModel.assert(MainState(initialNavGraph = NavGraphs.home)) {
                states(
                    { copy(isSessionActive = false) },
                )
            }
            verifyMocks()
        }

        @Test
        @DisplayName("WHEN the user has an expired session THEN the active session is cleared (success)")
        fun `the user has an expired session (success)`() = runTest {
            // GIVEN
            initMocks()
            coEitherJustRun { clearActiveSession() }

            // WHEN
            viewModel = MainViewModel(clearActiveSession, getAnilistToken, observeSession).test()
            viewModel.runOnCreate()
            viewModel.testIntent { clearSession() }

            // THEN
            coVerify(exactly = 1) { clearActiveSession() }
            verifyMocks()
        }

        @Test
        @DisplayName("WHEN the user has an expired session THEN the active session is cleared (failure)")
        fun `the user has an expired session (failure)`() = runTest {
            // GIVEN
            initMocks()
            coEvery { clearActiveSession() } returns mockk<SessionFailure>().left()

            // WHEN
            viewModel = MainViewModel(clearActiveSession, getAnilistToken, observeSession).test()
            viewModel.runOnCreate()
            viewModel.testIntent { clearSession() }

            // THEN
            coVerify(exactly = 1) { clearActiveSession() }
            verifyMocks()
        }

        @Test
        @DisplayName(
            """
            WHEN something wrong happens when observing the session
            THEN should update the session as not active
            """,
        )
        fun `something wrong happens when observing the session`() = runTest {
            // GIVEN
            initMocks(sessionFlow = flowOf(mockk<SessionFailure>().left()))

            // WHEN
            viewModel = MainViewModel(clearActiveSession, getAnilistToken, observeSession).test()
            viewModel.runOnCreate()

            // THEN
            viewModel.assert(MainState(initialNavGraph = NavGraphs.home)) {
                states(
                    { copy(isSessionActive = false) },
                )
            }
            verifyMocks()
        }

        private fun initMocks(
            sessionFlow: Flow<Either<Failure, Boolean>> = flowOf(true.right(), true.right(), false.right())
        ) {
            justRun { observeSession() }
            every { observeSession.flow } returns sessionFlow
            every { getAnilistToken.sync() } returns valueMockk<AnilistToken>().some()
        }

        private fun verifyMocks() {
            verify(exactly = 1) { observeSession() }
            verify(exactly = 1) { observeSession.flow }
            verify(exactly = 1) { getAnilistToken.sync() }
        }
    }

    @Nested
    @DisplayName("the user is logged out")
    inner class LoggedOut {
        @Test
        @DisplayName("WHEN the user does not have a saved token THEN the initial navGraph should be `LoginNavGraph`")
        fun `user does not have a saved token`() = runTest {
            // GIVEN
            justRun { observeSession() }
            every { observeSession.flow } returns emptyFlow()
            every { getAnilistToken.sync() } returns none()

            // WHEN
            viewModel = MainViewModel(clearActiveSession, getAnilistToken, observeSession).test()
            viewModel.runOnCreate()

            // THEN
            viewModel.assert(MainState(initialNavGraph = LoginNavGraph))
            verify(exactly = 1) { observeSession() }
            verify(exactly = 1) { observeSession.flow }
            verify(exactly = 1) { getAnilistToken.sync() }
        }
    }
}
