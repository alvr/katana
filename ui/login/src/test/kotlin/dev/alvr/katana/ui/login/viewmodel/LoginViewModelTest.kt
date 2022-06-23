package dev.alvr.katana.ui.login.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.token.failures.TokenPreferencesFailure
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.usecases.SaveAnilistTokenUseCase
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

internal class LoginViewModelTest : BehaviorSpec({
    val stateHandle = mockk<SavedStateHandle>(relaxed = true)
    val saveAnilistToken = mockk<SaveAnilistTokenUseCase>()
    val saveUserId = mockk<SaveUserIdUseCase>()

    // Return values
    val tokenWithParams = "my-token-from-anilist&param1=true&anotherOne=69420"
    val cleanToken = "my-token-from-anilist"

    xgiven("a deeplink without token") {
        every { stateHandle.get<String>(any()) } returns null
        coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
        coEvery { saveUserId() } returns Unit.right()

        and("a lazily created viewModel") {
            eventually {
                val viewModel by lazy { LoginViewModel(stateHandle, saveAnilistToken, saveUserId) }

                `when`("saving the token") {
                    then("it should not be saved because is null") {
                        viewModel.hashCode() // dummy call to initialize

                        coVerify(exactly = 0) { saveAnilistToken(AnilistToken(any())) }
                        coVerify(exactly = 0) { saveUserId() }
                    }
                }
            }
        }
    }

    xgiven("a deeplink with token with params") {
        every { stateHandle.get<String>(any()) } returns tokenWithParams

        and("a lazily created viewModel") {
            eventually {
                `when`("saving the token successfully") {
                    val viewModel by lazy { LoginViewModel(stateHandle, saveAnilistToken, saveUserId) }

                    coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
                    coEvery { saveUserId() } returns Unit.right()

                    then("it should not be saved without params") {
                        viewModel.hashCode() // dummy call to initialize

                        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(cleanToken)) }
                        coVerify(exactly = 1) { saveUserId() }
                    }
                }

                `when`("saving the token, it returns a left") {
                    val viewModel by lazy { LoginViewModel(stateHandle, saveAnilistToken, saveUserId) }

                    coEvery {
                        saveAnilistToken(AnilistToken(any()))
                    } returns TokenPreferencesFailure.SavingFailure.left()
                    coEvery { saveUserId() } returns Unit.right()

                    then("it should not be saved without params") {
                        viewModel.hashCode() // dummy call to initialize

                        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(cleanToken)) }
                        coVerify(exactly = 0) { saveUserId() }
                    }
                }
            }
        }
    }

    xgiven("a deeplink with token without params") {
        every { stateHandle.get<String>(any()) } returns cleanToken
        coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
        coEvery { saveUserId() } returns Unit.right()

        and("a lazily created viewModel") {
            eventually {
                val viewModel by lazy { LoginViewModel(stateHandle, saveAnilistToken, saveUserId) }

                `when`("saving the token") {
                    then("it should not be saved without params") {
                        viewModel.hashCode() // dummy call to initialize

                        coVerify(exactly = 1) { saveAnilistToken(AnilistToken(cleanToken)) }
                        coVerify(exactly = 1) { saveUserId() }
                    }
                }
            }
        }
    }

    xgiven("a deeplink that is empty") {
        every { stateHandle.get<String>(any()) } returns ""
        coEvery { saveAnilistToken(AnilistToken(any())) } returns Unit.right()
        coEvery { saveUserId() } returns Unit.right()

        and("a lazily created viewModel") {
            eventually {
                val viewModel by lazy { LoginViewModel(stateHandle, saveAnilistToken, saveUserId) }

                `when`("saving the token") {
                    then("it should not be saved because is empty") {
                        viewModel.hashCode() // dummy call to initialize

                        coVerify(exactly = 0) { saveAnilistToken(AnilistToken(any())) }
                        coVerify(exactly = 0) { saveUserId() }
                    }
                }
            }
        }
    }
},)
