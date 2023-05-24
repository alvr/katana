package dev.alvr.katana.ui.login.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.left
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.SaveSessionUseCase
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import dev.alvr.katana.ui.login.LOGIN_DEEP_LINK_TOKEN
import dev.alvr.katana.ui.login.R
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.orbitmvi.orbit.SuspendingTestContainerHost
import org.orbitmvi.orbit.test

@ExperimentalCoroutinesApi
internal class LoginViewModelTest : TestBase() {
    @RelaxedMockK
    private lateinit var stateHandle: SavedStateHandle
    @MockK
    private lateinit var saveAnilistToken: SaveSessionUseCase
    @MockK
    private lateinit var saveUserId: SaveUserIdUseCase

    private lateinit var viewModel: SuspendingTestContainerHost<LoginState, Nothing, LoginViewModel>

    override suspend fun beforeEach() {
        viewModel = LoginViewModel(stateHandle, saveAnilistToken, saveUserId).test(LoginState())
    }

    @ParameterizedTest(
        name = """
        GIVEN a deeplink without a valid token AND a lazily created viewModel
        WHEN saving the token
        THEN it should not be saved because is `{0}`
        """,
    )
    @NullSource
    @EmptySource
    fun `saving a token that is invalid`(token: String?) = runTest {
        // GIVEN
        every { stateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN) } returns token

        // WHEN
        viewModel.runOnCreate()
        viewModel.assert(LoginState()) {
            states()
        }

        // THEN
        verify(exactly = 1) { stateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN) }
        coVerify(exactly = 0) { saveAnilistToken(AnilistToken(any())) }
        coVerify(exactly = 0) { saveUserId() }
    }

    @ParameterizedTest(
        name = """
        GIVEN a deeplink with a valid token {0} AND a lazily created viewModel
        WHEN saving the token
        THEN it should be saved
        """,
    )
    @ValueSource(strings = [TOKEN_WITH_PARAMS, CLEAR_TOKEN])
    fun `saving a token that is valid`(token: String) = runTest {
        // GIVEN
        every { stateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN) } returns token
        coEitherJustRun { saveAnilistToken(AnilistToken(any())) }
        coEitherJustRun { saveUserId() }

        // WHEN
        viewModel.runOnCreate()
        viewModel.assert(LoginState()) {
            states(
                { copy(loading = true) },
                { copy(loading = false, saved = true) },
            )
        }

        // THEN
        verify(exactly = 1) { stateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN) }
        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
        coVerify(exactly = 1) { saveUserId() }
    }

    @ParameterizedTest(
        name = """
        GIVEN a deeplink with a valid token {0} AND a lazily created viewModel
        WHEN saving the token AND an errors occurs when saving the token
        THEN it should not be saved
        """,
    )
    @ValueSource(strings = [TOKEN_WITH_PARAMS, CLEAR_TOKEN])
    fun `saving a token that is valid AND an error occurs when saving the token`(token: String) = runTest {
        // GIVEN
        every { stateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN) } returns token
        coEvery { saveAnilistToken(AnilistToken(any())) } returns SessionFailure.SavingSession.left()

        // WHEN
        viewModel.runOnCreate()
        viewModel.assert(LoginState()) {
            states(
                { copy(loading = true) },
                { copy(loading = false, errorMessage = R.string.save_token_error) },
            )
        }

        // THEN
        verify(exactly = 1) { stateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN) }
        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
        coVerify(exactly = 0) { saveUserId() }
    }

    @ParameterizedTest(
        name = """
        GIVEN a deeplink with a valid token {0} AND a lazily created viewModel
        WHEN saving the token AND an errors occurs when saving the userId
        THEN it should not be saved
        """,
    )
    @ValueSource(strings = [TOKEN_WITH_PARAMS, CLEAR_TOKEN])
    fun `saving a token that is valid AND an error occurs when saving the userId`(token: String) = runTest {
        // GIVEN
        every { stateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN) } returns token
        coEitherJustRun { saveAnilistToken(AnilistToken(any())) }
        coEvery { saveUserId() } returns UserFailure.SavingUser.left()

        // WHEN
        viewModel.runOnCreate()
        viewModel.assert(LoginState()) {
            states(
                { copy(loading = true) },
                { copy(loading = false, errorMessage = R.string.fetch_userid_error) },
            )
        }

        // THEN
        verify(exactly = 1) { stateHandle.get<String>(LOGIN_DEEP_LINK_TOKEN) }
        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
        coVerify(exactly = 1) { saveUserId() }
    }

    private companion object {
        const val TOKEN_WITH_PARAMS = "my-token-from-anilist&param1=true&anotherOne=69420"
        const val CLEAR_TOKEN = "my-token-from-anilist"
    }
}
