package dev.alvr.katana.features.home.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
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
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class HomeViewModelTest : BehaviorSpec() {
    private val savedStateHandle = mockk<SavedStateHandle>()
    private val observeActiveSession = mockk<ObserveActiveSessionUseCase>()
    private val saveSession = mockk<SaveSessionUseCase>()
    private val saveUserId = mockk<SaveUserIdUseCase>()
    private val hideWelcomeCard = mockk<HideWelcomeCardUseCase>()
    private val observeWelcomeCardVisibility = mockk<ObserveWelcomeCardVisibilityUseCase>()

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

            `when`("observing the welcome card visibility") {
                and("is success") {
                    then("it should set showWelcomeCard to true") {
                        every { observeWelcomeCardVisibility.flow } returns flowOf(true.right())

                        viewModel.test { expectState { copy(forYouTab = forYouTab.copy(showWelcomeCard = true)) } }
                    }

                    then("it should set showWelcomeCard to false") {
                        every { observeWelcomeCardVisibility.flow } returns flowOf(false.right())

                        viewModel.test {
                            // do nothing, default value is false
                        }
                    }
                }

                and("there is an error") {
                    then("it should set showWelcomeCard to false") {
                        every { observeWelcomeCardVisibility.flow } returns
                            flowOf(HomeFailure.GettingWelcomeCardVisibility.left())

                        viewModel.test(initialStateWithCard) {
                            expectState { copy(forYouTab = forYouTab.copy(showWelcomeCard = false)) }
                        }
                    }
                }
            }
        }

        given("an intent") {
            beforeEach { initMocks() }

            and("is of type HomeIntent.ForYouIntent") {
                `when`("intent HomeIntent.ForYouIntent.CloseWelcomeCard") {
                    then("it should post HomeEffect.ForYouEffect.CloseWelcomeCard") {
                        viewModel.test { intent(HomeIntent.ForYouIntent.CloseWelcomeCard) }
                    }
                }

                `when`("intent HomeIntent.ForYouIntent.NavigateToAnimeLists") {
                    then("it should post HomeEffect.ForYouEffect.NavigateToAnimeLists") {
                        viewModel.test {
                            intent(HomeIntent.ForYouIntent.NavigateToAnimeLists)
                            expectEffect(HomeEffect.ForYouEffect.NavigateToAnimeLists)
                        }
                    }
                }

                `when`("intent HomeIntent.ForYouIntent.NavigateToMangaLists") {
                    then("it should post HomeEffect.ForYouEffect.NavigateToMangaLists") {
                        viewModel.test {
                            intent(HomeIntent.ForYouIntent.NavigateToMangaLists)
                            expectEffect(HomeEffect.ForYouEffect.NavigateToMangaLists)
                        }
                    }
                }

                `when`("intent HomeIntent.ForYouIntent.NavigateToTrending") {
                    then("it should post HomeEffect.ForYouEffect.NavigateToTrending") {
                        viewModel.test {
                            intent(HomeIntent.ForYouIntent.NavigateToTrending)
                            expectEffect(HomeEffect.ForYouEffect.NavigateToTrending)
                        }
                    }
                }

                `when`("intent HomeIntent.ForYouIntent.NavigateToPopular") {
                    then("it should post HomeEffect.ForYouEffect.NavigateToPopular") {
                        viewModel.test {
                            intent(HomeIntent.ForYouIntent.NavigateToPopular)
                            expectEffect(HomeEffect.ForYouEffect.NavigateToPopular)
                        }
                    }
                }

                `when`("intent HomeIntent.ForYouIntent.NavigateToUpcoming") {
                    then("it should post HomeEffect.ForYouEffect.NavigateToUpcoming") {
                        viewModel.test {
                            intent(HomeIntent.ForYouIntent.NavigateToUpcoming)
                            expectEffect(HomeEffect.ForYouEffect.NavigateToUpcoming)
                        }
                    }
                }

                `when`("intent HomeIntent.ForYouIntent.CloseWelcomeCard") {
                    and("is successful") {
                        coEitherJustRun { hideWelcomeCard() }

                        then("it should do nothing") {
                            viewModel.test { intent(HomeIntent.ForYouIntent.CloseWelcomeCard) }
                        }
                    }

                    and("is failure") {
                        coEvery { hideWelcomeCard() } returns HomeFailure.HidingWelcomeCard.left()

                        then("it should hide the card") {
                            viewModel.test(initialStateWithCard) {
                                intent(HomeIntent.ForYouIntent.CloseWelcomeCard)
                                expectState { copy(forYouTab = forYouTab.copy(showWelcomeCard = false)) }
                            }
                        }
                    }
                }
            }

            and("is of type HomeIntent.ActivityIntent") {
                `when`("intent HomeIntent.ActivityIntent") {
                    then("it should do nothing (yet)") { viewModel.test { intent(HomeIntent.ActivityIntent) } }
                }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        viewModel =
            HomeViewModel(
                savedStateHandle = savedStateHandle,
                hideWelcomeCardUseCase = hideWelcomeCard,
                observeActiveSessionUseCase = observeActiveSession,
                observeWelcomeCardVisibilityUseCase = observeWelcomeCardVisibility,
                saveSessionUseCase = saveSession,
                saveUserIdUseCase = saveUserId,
            )
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    private fun initMocks(token: String? = TOKEN_WITH_PARAMS, sessionActive: Boolean = false) {
        every { savedStateHandle.remove<String>("token") } returns token
        coJustRun { observeActiveSession() }
        coJustRun { observeWelcomeCardVisibility() }
        coEitherJustRun { saveSession(AnilistToken(any())) }
        coEitherJustRun { saveUserId() }
        every { observeActiveSession.flow } returns flowOf(sessionActive.right())
        every { observeWelcomeCardVisibility.flow } returns emptyFlow()
    }
}

private const val TOKEN_WITH_PARAMS = "my-token-from-anilist&param1=true&anotherOne=69420"
private const val CLEAR_TOKEN = "my-token-from-anilist"

private val initialStateWithCard = HomeState(forYouTab = HomeState.ForYouTabState(showWelcomeCard = true))
