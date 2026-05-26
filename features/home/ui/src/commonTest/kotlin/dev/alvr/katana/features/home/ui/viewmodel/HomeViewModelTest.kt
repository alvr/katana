package dev.alvr.katana.features.home.ui.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.home.ui.di.createHomeUiTestGraph
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import kotlinx.coroutines.flow.flowOf

internal class HomeViewModelTest : BehaviorSpec() {
    private val observeActiveSession = mock<ObserveActiveSessionUseCase>()
    private val saveSession = mock<SaveSessionUseCase>()
    private val saveUserId = mock<SaveUserIdUseCase>()

    private val invalidTokens = listOf(null, String.empty)
    private val validTokens = listOf(TOKEN_WITH_PARAMS, CLEAR_TOKEN)

    private lateinit var viewModel: HomeViewModel

    init {
        given("a logged out user") {
            and("a deeplink without a valid token") {
                `when`("saving the token") {
                    invalidTokens.forEach { token ->
                        then("it should NOT be saved because is `$token`") {
                            initMocks(token)

                            viewModel.test {
                                // only for run init()
                            }

                            verifySuspend(mode = VerifyMode.exactly(0)) { saveSession(any()) }
                            verifySuspend(mode = VerifyMode.exactly(0)) { saveUserId(Unit) }
                        }
                    }

                    validTokens.forEach { token ->
                        then("it should be saved because is `$token`") {
                            initMocks(token)
                            everySuspend { saveSession(any()) } returns Unit.right()
                            everySuspend { saveUserId(Unit) } returns Unit.right()

                            viewModel.test {
                                // only for run init()
                            }

                            verifySuspend(mode = VerifyMode.exactly(1)) { saveSession(AnilistToken(CLEAR_TOKEN)) }
                            verifySuspend(mode = VerifyMode.exactly(1)) { saveUserId(Unit) }
                        }
                    }

                    and("an error occurs when saving the token") {
                        validTokens.forEach { token ->
                            then("for token $token it should not be saved") {
                                initMocks(token)
                                everySuspend { saveSession(any()) } returns SessionFailure.SavingSession.left()

                                viewModel.test { expectEffect(HomeEffect.SaveTokenFailure) }

                                verifySuspend(mode = VerifyMode.exactly(1)) { saveSession(AnilistToken(CLEAR_TOKEN)) }
                                verifySuspend(mode = VerifyMode.exactly(0)) { saveUserId(Unit) }
                            }
                        }
                    }

                    and("an error occurs when saving the userId") {
                        validTokens.forEach { token ->
                            then("for token $token it should not be saved") {
                                initMocks(token)
                                everySuspend { saveSession(any()) } returns Unit.right()
                                everySuspend { saveUserId(Unit) } returns UserFailure.SavingUser.left()

                                viewModel.test { expectEffect(HomeEffect.SaveUserIdFailure) }

                                verifySuspend(mode = VerifyMode.exactly(1)) { saveSession(AnilistToken(CLEAR_TOKEN)) }
                                verifySuspend(mode = VerifyMode.exactly(1)) { saveUserId(Unit) }
                            }
                        }
                    }
                }
            }
        }

        given("an observer") {
            beforeEach { initMocks() }

            `when`("observing the session") {
                and("is success") {
                    then("it should set sessionActive to true") {
                        every { observeActiveSession.flow } returns flowOf(true.right())

                        viewModel.test { expectState { copy(sessionActive = true) } }
                    }
                }

                and("there is an error") {
                    beforeTest {
                        every { observeActiveSession.flow } returns flowOf(SessionFailure.CheckingActiveSession.left())
                    }

                    then("it should expect HomeEffect.ObserveSessionFailure effect") {
                        viewModel.test { expectEffect(HomeEffect.ObserveSessionFailure) }
                    }

                    then("it should set sessionActive to false") {
                        viewModel.test(HomeState(sessionActive = true)) {
                            expectState { copy(sessionActive = false) }
                            expectEffect(HomeEffect.ObserveSessionFailure)
                        }
                    }
                }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        viewModel =
            createHomeUiTestGraph(
                    observeActiveSessionUseCase = observeActiveSession,
                    saveSessionUseCase = saveSession,
                    saveUserIdUseCase = saveUserId,
                )
                .homeViewModelFactory
                .create(TOKEN_WITH_PARAMS)
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers(observeActiveSession, saveSession, saveUserId)
        resetCalls(observeActiveSession, saveSession, saveUserId)
    }

    private fun initMocks(token: String? = TOKEN_WITH_PARAMS, sessionActive: Boolean = false) {
        everySuspend { observeActiveSession(Unit) } returns Unit
        everySuspend { saveSession(any()) } returns Unit.right()
        everySuspend { saveUserId(Unit) } returns Unit.right()
        every { observeActiveSession.flow } returns flowOf(sessionActive.right())

        viewModel =
            createHomeUiTestGraph(
                    observeActiveSessionUseCase = observeActiveSession,
                    saveSessionUseCase = saveSession,
                    saveUserIdUseCase = saveUserId,
                )
                .homeViewModelFactory
                .create(token)
    }
}

private const val TOKEN_WITH_PARAMS = "my-token-from-anilist&param1=true&anotherOne=69420"
private const val CLEAR_TOKEN = "my-token-from-anilist"
