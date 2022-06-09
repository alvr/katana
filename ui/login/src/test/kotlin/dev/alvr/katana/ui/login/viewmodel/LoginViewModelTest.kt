package dev.alvr.katana.ui.login.viewmodel

import androidx.lifecycle.SavedStateHandle
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.usecases.SaveAnilistTokenUseCase
import dev.alvr.katana.domain.user.usecases.SaveUserIdUseCase
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coJustRun
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

    coJustRun { saveAnilistToken(AnilistToken(any())) }
    coJustRun { saveUserId() }

    given("a deeplink without token") {
        every { stateHandle.get<String>(any()) } returns null

        and("a lazily created viewModel") {
            eventually(retries = 10) {
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

    given("a deeplink with token with params") {
        every { stateHandle.get<String>(any()) } returns tokenWithParams

        and("a lazily created viewModel") {
            eventually(retries = 10) {
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

    given("a deeplink with token without params") {
        every { stateHandle.get<String>(any()) } returns cleanToken

        and("a lazily created viewModel") {
            eventually(retries = 10) {
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

    given("a deeplink that is empty") {
        every { stateHandle.get<String>(any()) } returns ""

        and("a lazily created viewModel") {
            eventually(retries = 10) {
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
