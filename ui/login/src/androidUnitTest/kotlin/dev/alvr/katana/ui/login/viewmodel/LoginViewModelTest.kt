package dev.alvr.katana.ui.login.viewmodel

import arrow.core.left
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.SaveSessionUseCase
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import dev.alvr.katana.ui.login.viewmodel.LoginState.ErrorType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.orbitmvi.orbit.test.test

@ExperimentalCoroutinesApi
internal class LoginViewModelTest : TestBase() {
    @MockK
    private lateinit var saveAnilistToken: SaveSessionUseCase

    @MockK
    private lateinit var saveUserId: SaveUserIdUseCase

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
        // WHEN
        LoginViewModel(token, saveAnilistToken, saveUserId).test(this) {
            runOnCreate()
            expectInitialState()
        }

        // THEN
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
        coEitherJustRun { saveAnilistToken(AnilistToken(any())) }
        coEitherJustRun { saveUserId() }

        // WHEN
        LoginViewModel(token, saveAnilistToken, saveUserId).test(this) {
            runOnCreate()
            expectInitialState()
            expectState { copy(loading = true) }
            expectState { copy(loading = false, saved = true) }
        }

        // THEN
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
    fun `saving a token that is valid AND an error occurs when saving the token`(token: String) =
        runTest {
            // GIVEN
            coEvery { saveAnilistToken(AnilistToken(any())) } returns SessionFailure.SavingSession.left()

            // WHEN
            LoginViewModel(token, saveAnilistToken, saveUserId).test(this) {
                runOnCreate()
                expectInitialState()
                expectState { copy(loading = true) }
                expectState { copy(loading = false, errorType = ErrorType.SaveToken) }
            }

            // THEN
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
    fun `saving a token that is valid AND an error occurs when saving the userId`(token: String) =
        runTest {
            // GIVEN
            coEitherJustRun { saveAnilistToken(AnilistToken(any())) }
            coEvery { saveUserId() } returns UserFailure.SavingUser.left()

            // WHEN
            LoginViewModel(token, saveAnilistToken, saveUserId).test(this) {
                runOnCreate()
                expectInitialState()
                expectState { copy(loading = true) }
                expectState { copy(loading = false, errorType = ErrorType.SaveUserId) }
            }

            // THEN
            coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
            coVerify(exactly = 1) { saveUserId() }
        }

    private companion object {
        const val TOKEN_WITH_PARAMS = "my-token-from-anilist&param1=true&anotherOne=69420"
        const val CLEAR_TOKEN = "my-token-from-anilist"
    }
}
