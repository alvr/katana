package dev.alvr.katana.features.account.ui.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.orbitTestScope
import dev.alvr.katana.features.account.ui.entities.UserInfoUi
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.test.test

internal class AccountViewModelTest : FreeSpec() {
    private val observeUserInfoUseCase = mockk<ObserveUserInfoUseCase>()
    private val logOutUseCase = mockk<LogOutUseCase>()

    private lateinit var viewModel: AccountViewModel

    init {
        "clearing the session" {
            coEvery { logOutUseCase() } returns Unit.right()

            viewModel.test(orbitTestScope) {
                expectInitialState()
                containerHost.clearSession()
                cancelAndIgnoreRemainingItems()
            }

            coVerify(exactly = 1) { logOutUseCase() }
        }

        "observing the user info" - {
            "successfully" {
                every { observeUserInfoUseCase.flow } returns flowOf(userInfo.right())
                coJustRun { observeUserInfoUseCase() }

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectState { copy(userInfo = userInfoUi, isLoading = false, isError = false) }
                    cancelAndIgnoreRemainingItems()
                }

                coVerify(exactly = 1) { observeUserInfoUseCase() }
                verify(exactly = 1) { observeUserInfoUseCase.flow }
            }

            "unsuccessfully" {
                every { observeUserInfoUseCase.flow } returns flowOf(UserFailure.GettingUserInfo.left())
                coJustRun { observeUserInfoUseCase() }

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectState { copy(isLoading = false, isError = true) }
                    cancelAndIgnoreRemainingItems()
                }

                coVerify(exactly = 1) { observeUserInfoUseCase() }
                verify(exactly = 1) { observeUserInfoUseCase.flow }
            }
        }
    }

    override suspend fun beforeTest(testCase: TestCase) {
        viewModel = AccountViewModel(observeUserInfoUseCase, logOutUseCase)
    }

    override suspend fun afterAny(testCase: TestCase, result: TestResult) {
        clearAllMocks()
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
