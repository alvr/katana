package dev.alvr.katana.features.home.ui.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ForYouUiTestGraph : TestAppGraph {
    val forYouViewModel: ForYouViewModel

    /**
     * Creates a ForYouViewModel configured with the provided use cases.
     *
     * @param hideWelcomeCardUseCase Use case that hides the welcome card when invoked.
     * @param observeWelcomeCardVisibilityUseCase Use case that observes welcome card visibility changes.
     * @return A configured ForYouViewModel instance.
     */
    @Provides
    fun forYouViewModel(
        hideWelcomeCardUseCase: HideWelcomeCardUseCase,
        observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
    ): ForYouViewModel = ForYouViewModel(hideWelcomeCardUseCase, observeWelcomeCardVisibilityUseCase)

    @DependencyGraph.Factory
    interface Factory {
        /**
         * Creates a ForYouUiTestGraph initialized with the given use cases.
         *
         * @param hideWelcomeCardUseCase Use case that hides the welcome card.
         * @param observeWelcomeCardVisibilityUseCase Use case that observes the welcome card's visibility.
         * @return A ForYouUiTestGraph wired with the provided use cases.
         */
        fun create(
            @Provides hideWelcomeCardUseCase: HideWelcomeCardUseCase,
            @Provides observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
        ): ForYouUiTestGraph
    }
}

/**
         * Creates a test dependency graph for the "For You" home UI and obtains its view model.
         *
         * @param hideWelcomeCardUseCase Use case that hides the welcome card.
         * @param observeWelcomeCardVisibilityUseCase Use case that observes the welcome card's visibility.
         * @return The initialized ForYouViewModel from the test graph.
         */
        internal fun createForYouUiTestGraph(
    hideWelcomeCardUseCase: HideWelcomeCardUseCase,
    observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
) =
    createGraphFactory<ForYouUiTestGraph.Factory>()
        .create(
            hideWelcomeCardUseCase = hideWelcomeCardUseCase,
            observeWelcomeCardVisibilityUseCase = observeWelcomeCardVisibilityUseCase,
        )
        .forYouViewModel
