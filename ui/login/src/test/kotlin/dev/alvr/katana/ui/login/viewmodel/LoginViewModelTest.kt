package dev.alvr.katana.ui.login.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.usecases.SaveSessionUseCase
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import dev.alvr.katana.ui.login.R
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.orbitmvi.orbit.test

internal class LoginViewModelTest : BehaviorSpec() {
    private val stateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val saveAnilistToken = mockk<SaveSessionUseCase>()
    private val saveUserId = mockk<SaveUserIdUseCase>()
    private val viewModel = LoginViewModel(stateHandle, saveAnilistToken, saveUserId).test(LoginState())

    init {
        given("a deeplink without token") {
            every { stateHandle.get<String>(any()) } returns null
            coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
            coEvery { saveUserId() } returns Unit.right()

            and("a lazily created viewModel") {
                `when`("saving the token") {
                    then("it should not be saved because is null") {
                        viewModel.runOnCreate()
                        viewModel.assert(LoginState()) {
                            states()
                        }

                        coVerify(exactly = 0) { saveAnilistToken(AnilistToken(any())) }
                        coVerify(exactly = 0) { saveUserId() }
                    }
                }
            }
        }

        given("a deeplink with token with params") {
            every { stateHandle.get<String>(any()) } returns TOKEN_WITH_PARAMS

            and("a lazily created viewModel") {
                `when`("saving the token successfully") {
                    coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
                    coEvery { saveUserId() } returns Unit.right()

                    then("it should not be saved without params") {
                        viewModel.runOnCreate()
                        viewModel.assert(LoginState()) {
                            states(
                                { copy(loading = true) },
                                { copy(loading = false, saved = true) },
                            )
                        }

                        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
                        coVerify(exactly = 1) { saveUserId() }
                    }
                }

                `when`("saving the token, it returns a left") {
                    coEvery { saveAnilistToken(AnilistToken(any())) } returns SessionFailure.SavingSession.left()
                    coEvery { saveUserId() } returns Unit.right()

                    then("it should not be saved without params") {
                        viewModel.runOnCreate()
                        viewModel.assert(LoginState()) {
                            states(
                                { copy(loading = true) },
                                { copy(loading = false, errorMessage = R.string.save_token_error) },
                            )
                        }

                        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
                        coVerify(exactly = 0) { saveUserId() }
                    }
                }

                `when`("saving the userId, it returns a left") {
                    coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
                    coEvery { saveUserId() } returns UserFailure.SavingUser.left()

                    then("it should not be saved without params") {
                        viewModel.runOnCreate()
                        viewModel.assert(LoginState()) {
                            states(
                                { copy(loading = true) },
                                { copy(loading = false, errorMessage = R.string.fetch_userid_error) },
                            )
                        }

                        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
                        coVerify(exactly = 1) { saveUserId() }
                    }
                }
            }
        }

        given("a deeplink with token without params") {
            every { stateHandle.get<String>(any()) } returns CLEAR_TOKEN
            coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
            coEvery { saveUserId() } returns Unit.right()

            and("a lazily created viewModel") {
                `when`("saving the token") {
                    then("it should not be saved without params") {
                        viewModel.runOnCreate()
                        viewModel.assert(LoginState()) {
                            states(
                                { copy(loading = true) },
                                { copy(loading = false, saved = true) },
                            )
                        }

                        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(CLEAR_TOKEN)) }
                        coVerify(exactly = 1) { saveUserId() }
                    }
                }
            }
        }

        given("a deeplink that is empty") {
            every { stateHandle.get<String>(any()) } returns String.empty
            coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
            coEvery { saveUserId() } returns Unit.right()

            and("a lazily created viewModel") {
                `when`("saving the token") {
                    viewModel.runOnCreate()
                    viewModel.assert(LoginState()) {
                        states()
                    }

                    then("it should not be saved because is empty") {
                        coVerify(exactly = 0) { saveAnilistToken(AnilistToken(any())) }
                        coVerify(exactly = 0) { saveUserId() }
                    }
                }
            }
        }
    }

    private companion object {
        const val TOKEN_WITH_PARAMS = "my-token-from-anilist&param1=true&anotherOne=69420"
        const val CLEAR_TOKEN = "my-token-from-anilist"
    }
}
