package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.media.domain.usecases.ObserveMediaCollectionUseCase
import dev.alvr.katana.core.tests.ui.FinalizationType
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObservePopularMediaUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveTrendingMediaUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveUpcomingAnimeUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.alvr.katana.features.home.ui.di.createForYouUiTestGraph
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class ForYouViewModelTest : BehaviorSpec() {
    private val hideWelcomeCard = mock<HideWelcomeCardUseCase>()
    private val observePopularMedia = mock<ObservePopularMediaUseCase>()
    private val observeTrendingMedia = mock<ObserveTrendingMediaUseCase>()
    private val observeUpcomingAnime = mock<ObserveUpcomingAnimeUseCase>()
    private val observeMediaCollection = mock<ObserveMediaCollectionUseCase>()
    private val observeWelcomeCardVisibility = mock<ObserveWelcomeCardVisibilityUseCase>()

    init {
        given("an observer") {
            `when`("observing the welcome card visibility") {
                and("is success") {
                    then("it should set showWelcomeCard to true") {
                        every { observeWelcomeCardVisibility.flow } returns flowOf(true.right())

                        createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                            currentState.showWelcomeCard shouldBe true
                        }
                    }

                    then("it should set showWelcomeCard to false") {
                        every { observeWelcomeCardVisibility.flow } returns flowOf(false.right())

                        createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                            // do nothing, default value is false
                        }
                    }
                }

                and("there is an error") {
                    then("it should set showWelcomeCard to false") {
                        every { observeWelcomeCardVisibility.flow } returns
                            flowOf(true.right(), HomeFailure.GettingWelcomeCardVisibility.left())

                        createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                            currentState.showWelcomeCard shouldBe false
                        }
                    }
                }
            }
        }

        given("an intent") {
            `when`("intent ForYouIntent.CloseWelcomeCard") {
                and("is successful") {
                    everySuspend { hideWelcomeCard(Unit) } returns Unit.right()

                    then("it should do nothing") {
                        createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                            intent(ForYouIntent.CloseWelcomeCard)
                        }
                    }
                }

                and("is failure") {
                    then("it should hide the card") {
                        everySuspend { hideWelcomeCard(Unit) } returns HomeFailure.HidingWelcomeCard.left()
                        every { observeWelcomeCardVisibility.flow } returns flowOf(true.right())

                        createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                            currentState.showWelcomeCard shouldBe true
                            intent(ForYouIntent.CloseWelcomeCard)
                            expectState { copy(showWelcomeCard = false) }
                        }
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToAnimeLists") {
                then("it should post ForYouEffect.NavigateToAnimeLists") {
                    createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                        intent(ForYouIntent.NavigateToAnimeLists)
                        expectEffect(ForYouEffect.NavigateToAnimeLists)
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToMangaLists") {
                then("it should post ForYouEffect.NavigateToMangaLists") {
                    createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                        intent(ForYouIntent.NavigateToMangaLists)
                        expectEffect(ForYouEffect.NavigateToMangaLists)
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToTrending") {
                then("it should post ForYouEffect.NavigateToTrending") {
                    createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                        intent(ForYouIntent.NavigateToTrending)
                        expectEffect(ForYouEffect.NavigateToTrending)
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToPopular") {
                then("it should post ForYouEffect.NavigateToPopular") {
                    createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                        intent(ForYouIntent.NavigateToPopular)
                        expectEffect(ForYouEffect.NavigateToPopular)
                    }
                }
            }

            `when`("intent ForYouIntent.NavigateToUpcoming") {
                then("it should post ForYouEffect.NavigateToUpcoming") {
                    createForYouViewModel().test(finalizationType = FinalizationType.Drop) {
                        intent(ForYouIntent.NavigateToUpcoming)
                        expectEffect(ForYouEffect.NavigateToUpcoming)
                    }
                }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        everySuspend { observePopularMedia(any()) } returns Unit
        everySuspend { observeTrendingMedia(any()) } returns Unit
        everySuspend { observeUpcomingAnime(any()) } returns Unit
        everySuspend { observeMediaCollection(any()) } returns Unit
        everySuspend { observeWelcomeCardVisibility(any()) } returns Unit
        every { observePopularMedia.flow } returns emptyFlow()
        every { observeTrendingMedia.flow } returns emptyFlow()
        every { observeUpcomingAnime.flow } returns emptyFlow()
        every { observeMediaCollection.flow } returns emptyFlow()
        every { observeWelcomeCardVisibility.flow } returns emptyFlow()
    }

    private fun createForYouViewModel(): ForYouViewModel =
        createForYouUiTestGraph(
                hideWelcomeCardUseCase = hideWelcomeCard,
                observePopularMediaUseCase = observePopularMedia,
                observeTrendingMediaUseCase = observeTrendingMedia,
                observeUpcomingAnimeUseCase = observeUpcomingAnime,
                observeMediaCollectionUseCase = observeMediaCollection,
                observeWelcomeCardVisibilityUseCase = observeWelcomeCardVisibility,
            )
            .forYouViewModel as ForYouViewModel

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers(
            hideWelcomeCard,
            observePopularMedia,
            observeTrendingMedia,
            observeUpcomingAnime,
            observeMediaCollection,
            observeWelcomeCardVisibility,
        )
        resetCalls(
            hideWelcomeCard,
            observePopularMedia,
            observeTrendingMedia,
            observeUpcomingAnime,
            observeMediaCollection,
            observeWelcomeCardVisibility,
        )
    }
}
