package dev.alvr.katana.features.account.ui.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.account.ui.di.createAccountUiTestGraph
import dev.alvr.katana.features.account.ui.entities.UserInfoUi
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class AccountViewModelTest : FreeSpec() {
    private val observeUserInfoUseCase = mock<ObserveUserInfoUseCase>()
    private val logOutUseCase = mock<LogOutUseCase>()

    private lateinit var viewModel: AccountViewModel

    init {
        "observing the user info" -
            {
                "successfully" {
                    every { observeUserInfoUseCase.flow } returns flowOf(userInfo.right())

                    viewModel.test { expectState { copy(userInfo = userInfoUi, loading = false, error = false) } }

                    verifySuspend(mode = VerifyMode.exactly(1)) { observeUserInfoUseCase(Unit) }
                    verify(mode = VerifyMode.exactly(1)) { observeUserInfoUseCase.flow }
                }

                "unsuccessfully" {
                    every { observeUserInfoUseCase.flow } returns flowOf(UserFailure.GettingUserInfo.left())

                    viewModel.test { expectState { copy(loading = false, error = true) } }

                    verifySuspend(mode = VerifyMode.exactly(1)) { observeUserInfoUseCase(Unit) }
                    verify(mode = VerifyMode.exactly(1)) { observeUserInfoUseCase.flow }
                }
            }

        "logging out from the account" -
            {
                "is successful" {
                    every { observeUserInfoUseCase.flow } returns flowOf(userInfo.right())
                    everySuspend { logOutUseCase(Unit) } returns Unit.right()

                    viewModel.test {
                        expectState { copy(userInfo = userInfoUi, loading = false, error = false) }
                        intent(AccountIntent.Logout)
                        expectState { copy(userInfo = null) }
                        expectEffect(AccountEffect.LoggingOutSuccess)
                    }

                    verifySuspend(mode = VerifyMode.exactly(1)) { logOutUseCase(Unit) }
                }

                "is failure" {
                    every { observeUserInfoUseCase.flow } returns flowOf(userInfo.right())
                    everySuspend { logOutUseCase(Unit) } returns SessionFailure.LoggingOut.left()

                    viewModel.test {
                        expectState { copy(userInfo = userInfoUi, loading = false, error = false) }
                        intent(AccountIntent.Logout)
                        expectState { copy(userInfo = null) }
                        expectEffect(AccountEffect.LoggingOutFailure)
                    }

                    verifySuspend(mode = VerifyMode.exactly(1)) { logOutUseCase(Unit) }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        everySuspend { observeUserInfoUseCase(Unit) } returns Unit
        every { observeUserInfoUseCase.flow } returns emptyFlow()

        viewModel =
            createAccountUiTestGraph(observeUserInfoUseCase = observeUserInfoUseCase, logOutUseCase = logOutUseCase)
                .accountViewModel
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers(observeUserInfoUseCase, logOutUseCase)
        resetCalls(observeUserInfoUseCase, logOutUseCase)
    }
}

private val userInfo = UserInfo(username = "username", avatar = "avatar", banner = "banner")

private val userInfoUi = UserInfoUi(username = "username", avatar = "avatar", banner = "banner")
