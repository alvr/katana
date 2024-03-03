package dev.alvr.katana.features.login.ui.viewmodel

import arrow.core.left
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.coEitherJustRun
import dev.alvr.katana.core.tests.orbitTestScope
import dev.alvr.katana.features.login.ui.viewmodel.LoginState.ErrorType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.orbitmvi.orbit.test.test

internal class LoginViewModelTest : BehaviorSpec() {
    private val saveAnilistToken = mockk<SaveSessionUseCase>()
    private val saveUserId = mockk<SaveUserIdUseCase>()

    private val invalidTokens = listOf(null, String.empty)
    private val validTokens = listOf(TOKEN_WITH_PARAMS, CLEAR_TOKEN)

    init {
        given("a deeplink without a valid token") {
            `when`("saving the token") {
                invalidTokens.forEach { token ->
                    then("it should NOT be saved because is `$token`") {
                        LoginViewModel(token, saveAnilistToken, saveUserId).test(orbitTestScope) {
                            runOnCreate()
                            expectInitialState()
                        }

                        coVerify(exactly = 0) { saveAnilistToken(AnilistToken(any())) }
                        coVerify(exactly = 0) { saveUserId() }
                    }
                }

                validTokens.forEach { token ->
                    then("it should be saved because is `$token`") {
                        coEitherJustRun { saveAnilistToken(AnilistToken(any())) }
                        coEitherJustRun { saveUserId() }

                        LoginViewModel(token, saveAnilistToken, saveUserId).test(orbitTestScope) {
                            runOnCreate()
                            expectInitialState()
                            expectState { copy(loading = true) }
                            expectState { copy(loading = false, saved = true) }
                        }

                        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
                        coVerify(exactly = 1) { saveUserId() }
                    }
                }

                and("an error occurs when saving the token") {
                    validTokens.forEach { token ->
                        then("for token $token it should not be saved") {
                            coEvery { saveAnilistToken(AnilistToken(any())) } returns
                                SessionFailure.SavingSession.left()

                            LoginViewModel(
                                token,
                                saveAnilistToken,
                                saveUserId,
                            ).test(orbitTestScope) {
                                runOnCreate()
                                expectInitialState()
                                expectState { copy(loading = true) }
                                expectState {
                                    copy(
                                        loading = false,
                                        errorType = ErrorType.SaveToken,
                                    )
                                }
                            }

                            coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
                            coVerify(exactly = 0) { saveUserId() }
                        }
                    }
                }

                and("an error occurs when saving the userId") {
                    validTokens.forEach { token ->
                        then("for token $token it should not be saved") {
                            coEitherJustRun { saveAnilistToken(AnilistToken(any())) }
                            coEvery { saveUserId() } returns UserFailure.SavingUser.left()

                            LoginViewModel(
                                token,
                                saveAnilistToken,
                                saveUserId,
                            ).test(orbitTestScope) {
                                runOnCreate()
                                expectInitialState()
                                expectState { copy(loading = true) }
                                expectState {
                                    copy(
                                        loading = false,
                                        errorType = ErrorType.SaveUserId,
                                    )
                                }
                            }

                            coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
                            coVerify(exactly = 1) { saveUserId() }
                        }
                    }
                }
            }
        }
    }

    override suspend fun afterAny(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    private companion object {
        const val TOKEN_WITH_PARAMS = "my-token-from-anilist&param1=true&anotherOne=69420"
        const val CLEAR_TOKEN = "my-token-from-anilist"
    }
}
