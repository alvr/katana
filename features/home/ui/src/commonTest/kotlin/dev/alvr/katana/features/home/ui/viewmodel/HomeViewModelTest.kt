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
import dev.alvr.katana.core.domain.failures.Failure
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
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class HomeViewModelTest : BehaviorSpec() {
    private val observeActiveSession = mock<ObserveActiveSessionUseCase>()
    private val saveSession = mock<SaveSessionUseCase>()
    private val saveUserId = mock<SaveUserIdUseCase>()

    private val invalidTokens = listOf(null, String.empty)
    private val validTokens = listOf(TOKEN_WITH_PARAMS, CLEAR_TOKEN)

    init {
        given("a logged out user") {
            and("a deeplink without a valid token") {
                `when`("saving the token") {
                    invalidTokens.forEach { token ->
                        then("it should NOT be saved because is `$token`") {
                            val viewModel = createHomeViewModel(token)

                            viewModel.test {
                                // only for run init()
                            }

                            verifySuspend(mode = VerifyMode.exactly(0)) { saveSession(any()) }
                            verifySuspend(mode = VerifyMode.exactly(0)) { saveUserId(Unit) }
                        }
                    }

                    validTokens.forEach { token ->
                        then("it should be saved because is `$token`") {
                            val viewModel = createHomeViewModel(token)

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
                                val viewModel =
                                    createHomeViewModel(
                                        token = token,
                                        saveSessionResult = SessionFailure.SavingSession.left(),
                                    )

                                viewModel.test { expectEffect(HomeEffect.SaveTokenFailure) }

                                verifySuspend(mode = VerifyMode.exactly(1)) { saveSession(AnilistToken(CLEAR_TOKEN)) }
                                verifySuspend(mode = VerifyMode.exactly(0)) { saveUserId(Unit) }
                            }
                        }
                    }

                    and("an error occurs when saving the userId") {
                        validTokens.forEach { token ->
                            then("for token $token it should not be saved") {
                                val viewModel =
                                    createHomeViewModel(
                                        token = token,
                                        saveSessionResult = Unit.right(),
                                        saveUserIdResult = UserFailure.SavingUser.left(),
                                    )

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
            `when`("observing the session") {
                and("is success") {
                    then("it should set sessionActive to true") {
                        val viewModel = createHomeViewModel(token = null, sessionFlow = flowOf(true.right()))

                        viewModel.test { expectState { copy(sessionActive = true) } }
                    }
                }

                and("there is an error") {
                    then("it should expect HomeEffect.ObserveSessionFailure effect") {
                        val viewModel =
                            createHomeViewModel(
                                token = null,
                                sessionFlow = flowOf(SessionFailure.CheckingActiveSession.left()),
                            )

                        viewModel.test { expectEffect(HomeEffect.ObserveSessionFailure) }
                    }

                    then("it should set sessionActive to false") {
                        val viewModel =
                            createHomeViewModel(
                                token = null,
                                sessionFlow = flowOf(true.right(), SessionFailure.CheckingActiveSession.left()),
                            )

                        viewModel.test {
                            currentState.sessionActive shouldBe false
                            expectEffect(HomeEffect.ObserveSessionFailure)
                        }
                    }
                }
            }
        }
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers(observeActiveSession, saveSession, saveUserId)
        resetCalls(observeActiveSession, saveSession, saveUserId)
    }

    private fun createHomeViewModel(
        token: String?,
        sessionFlow: Flow<arrow.core.Either<Failure, Boolean>> = flowOf(false.right()),
        saveSessionResult: arrow.core.Either<Failure, Unit> = Unit.right(),
        saveUserIdResult: arrow.core.Either<Failure, Unit> = Unit.right(),
    ): HomeViewModel {
        everySuspend { observeActiveSession(Unit) } returns Unit
        everySuspend { saveSession(any()) } returns saveSessionResult
        everySuspend { saveUserId(Unit) } returns saveUserIdResult
        every { observeActiveSession.flow } returns sessionFlow

        return createHomeUiTestGraph(
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
