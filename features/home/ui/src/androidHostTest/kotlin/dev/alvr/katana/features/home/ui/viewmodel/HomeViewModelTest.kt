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
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.coEitherJustRun
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.home.ui.di.createHomeUiTestGraph
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

internal class HomeViewModelTest : BehaviorSpec() {
    private val observeActiveSession = mockk<ObserveActiveSessionUseCase>()
    private val saveSession = mockk<SaveSessionUseCase>()
    private val saveUserId = mockk<SaveUserIdUseCase>()

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

                            coVerify(exactly = 0) { saveSession(AnilistToken(any())) }
                            coVerify(exactly = 0) { saveUserId() }
                        }
                    }

                    validTokens.forEach { token ->
                        then("it should be saved because is `$token`") {
                            initMocks(token)
                            coEitherJustRun { saveSession(AnilistToken(any())) }
                            coEitherJustRun { saveUserId() }

                            viewModel.test {
                                // only for run init()
                            }

                            coVerify(exactly = 1) { saveSession(AnilistToken(CLEAR_TOKEN)) }
                            coVerify(exactly = 1) { saveUserId() }
                        }
                    }

                    and("an error occurs when saving the token") {
                        validTokens.forEach { token ->
                            then("for token $token it should not be saved") {
                                initMocks(token)
                                coEvery { saveSession(AnilistToken(any())) } returns SessionFailure.SavingSession.left()

                                viewModel.test { expectEffect(HomeEffect.SaveTokenFailure) }

                                coVerify(exactly = 1) { saveSession(AnilistToken(CLEAR_TOKEN)) }
                                coVerify(exactly = 0) { saveUserId() }
                            }
                        }
                    }

                    and("an error occurs when saving the userId") {
                        validTokens.forEach { token ->
                            then("for token $token it should not be saved") {
                                initMocks(token)
                                coEitherJustRun { saveSession(AnilistToken(any())) }
                                coEvery { saveUserId() } returns UserFailure.SavingUser.left()

                                viewModel.test { expectEffect(HomeEffect.SaveUserIdFailure) }

                                coVerify(exactly = 1) { saveSession(AnilistToken(CLEAR_TOKEN)) }
                                coVerify(exactly = 1) { saveUserId() }
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
                token = TOKEN_WITH_PARAMS,
                observeActiveSessionUseCase = observeActiveSession,
                saveSessionUseCase = saveSession,
                saveUserIdUseCase = saveUserId,
            )
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    private fun initMocks(token: String? = TOKEN_WITH_PARAMS, sessionActive: Boolean = false) {
        coJustRun { observeActiveSession() }
        coEitherJustRun { saveSession(AnilistToken(any())) }
        coEitherJustRun { saveUserId() }
        every { observeActiveSession.flow } returns flowOf(sessionActive.right())

        viewModel =
            createHomeUiTestGraph(
                token = token,
                observeActiveSessionUseCase = observeActiveSession,
                saveSessionUseCase = saveSession,
                saveUserIdUseCase = saveUserId,
            )
    }
}

private const val TOKEN_WITH_PARAMS = "my-token-from-anilist&param1=true&anotherOne=69420"
private const val CLEAR_TOKEN = "my-token-from-anilist"
