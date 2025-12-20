package dev.alvr.katana.features.account.ui.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.account.ui.entities.UserInfoUi
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

internal class AccountViewModelTest : FreeSpec() {
    private val observeUserInfoUseCase = mockk<ObserveUserInfoUseCase>()
    private val logOutUseCase = mockk<LogOutUseCase>()

    private lateinit var viewModel: AccountViewModel

    init {
        "observing the user info" - {
            "successfully" {
                every { observeUserInfoUseCase.flow } returns flowOf(userInfo.right())
                coJustRun { observeUserInfoUseCase() }

                viewModel.test {
                    expectState { copy(userInfo = userInfoUi, loading = false, error = false) }
                }

                coVerify(exactly = 1) { observeUserInfoUseCase() }
                verify(exactly = 1) { observeUserInfoUseCase.flow }
            }

            "unsuccessfully" {
                every { observeUserInfoUseCase.flow } returns flowOf(UserFailure.GettingUserInfo.left())
                coJustRun { observeUserInfoUseCase() }

                viewModel.test {
                    expectState { copy(loading = false, error = true) }
                }

                coVerify(exactly = 1) { observeUserInfoUseCase() }
                verify(exactly = 1) { observeUserInfoUseCase.flow }
            }
        }

        "logging out from the account" - {
            "is successful" {
                coEvery { logOutUseCase() } returns Unit.right()

                viewModel.test(AccountState(userInfo = UserInfoUi())) {
                    intent(AccountIntent.Logout)
                    expectState { copy(userInfo = null) }
                    expectEffect(AccountEffect.LoggingOutSuccess)
                }

                coVerify(exactly = 1) { logOutUseCase() }
            }

            "is failure" {
                coEvery { logOutUseCase() } returns SessionFailure.LoggingOut.left()

                viewModel.test(AccountState(userInfo = UserInfoUi())) {
                    intent(AccountIntent.Logout)
                    expectState { copy(userInfo = null) }
                    expectEffect(AccountEffect.LoggingOutFailure)
                }

                coVerify(exactly = 1) { logOutUseCase() }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        clearAllMocks()
        viewModel = AccountViewModel(observeUserInfoUseCase, logOutUseCase)
    }

    private companion object {
        val userInfo = UserInfo(
            username = "username",
            avatar = "avatar",
            banner = "banner",
        )

        val userInfoUi = UserInfoUi(
            username = "username",
            avatar = "avatar",
            banner = "banner",
        )
    }
}
