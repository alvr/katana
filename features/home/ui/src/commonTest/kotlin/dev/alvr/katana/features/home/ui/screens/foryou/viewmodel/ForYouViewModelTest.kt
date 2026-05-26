package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.alvr.katana.features.home.ui.di.createForYouUiTestGraph
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class ForYouViewModelTest : BehaviorSpec() {
    private val hideWelcomeCard = mock<HideWelcomeCardUseCase>()
    private val observeWelcomeCardVisibility = mock<ObserveWelcomeCardVisibilityUseCase>()

    private lateinit var viewModel: ForYouViewModel

    init {
        given("an observer") {
            `when`("observing the welcome card visibility") {
                and("is success") {
                    then("it should set showWelcomeCard to true") {
                        every { observeWelcomeCardVisibility.flow } returns flowOf(true.right())

                        viewModel.test { expectState { copy(showWelcomeCard = true) } }
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

                        viewModel.test(initialStateWithCard) { expectState { copy(showWelcomeCard = false) } }
                    }
                }
            }
        }

        given("an intent") {
            `when`("intent ForYouIntent.CloseWelcomeCard") {
                and("is successful") {
                    everySuspend { hideWelcomeCard(Unit) } returns Unit.right()

                    then("it should do nothing") { viewModel.test { intent(ForYouIntent.CloseWelcomeCard) } }
                }

                and("is failure") {
                    everySuspend { hideWelcomeCard(Unit) } returns HomeFailure.HidingWelcomeCard.left()

                    then("it should hide the card") {
                        viewModel.test(initialStateWithCard) {
                            intent(ForYouIntent.CloseWelcomeCard)
                            expectState { copy(showWelcomeCard = false) }
                        }
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToAnimeLists") {
                then("it should post ForYouEffect.NavigateToAnimeLists") {
                    viewModel.test {
                        intent(ForYouIntent.NavigateToAnimeLists)
                        expectEffect(ForYouEffect.NavigateToAnimeLists)
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToMangaLists") {
                then("it should post ForYouEffect.NavigateToMangaLists") {
                    viewModel.test {
                        intent(ForYouIntent.NavigateToMangaLists)
                        expectEffect(ForYouEffect.NavigateToMangaLists)
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToTrending") {
                then("it should post ForYouEffect.NavigateToTrending") {
                    viewModel.test {
                        intent(ForYouIntent.NavigateToTrending)
                        expectEffect(ForYouEffect.NavigateToTrending)
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToPopular") {
                then("it should post ForYouEffect.NavigateToPopular") {
                    viewModel.test {
                        intent(ForYouIntent.NavigateToPopular)
                        expectEffect(ForYouEffect.NavigateToPopular)
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToUpcoming") {
                then("it should post ForYouEffect.NavigateToUpcoming") {
                    viewModel.test {
                        intent(ForYouIntent.NavigateToUpcoming)
                        expectEffect(ForYouEffect.NavigateToUpcoming)
                    }
                }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        everySuspend { observeWelcomeCardVisibility(Unit) } returns Unit
        every { observeWelcomeCardVisibility.flow } returns emptyFlow()
        viewModel =
            createForYouUiTestGraph(
                hideWelcomeCardUseCase = hideWelcomeCard,
                observeWelcomeCardVisibilityUseCase = observeWelcomeCardVisibility,
            )
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers(hideWelcomeCard, observeWelcomeCardVisibility)
        resetCalls(hideWelcomeCard, observeWelcomeCardVisibility)
    }
}

private val initialStateWithCard = ForYouState(showWelcomeCard = true)
