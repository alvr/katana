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

    @Provides
    fun forYouViewModel(
        hideWelcomeCardUseCase: HideWelcomeCardUseCase,
        observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
    ): ForYouViewModel = ForYouViewModel(hideWelcomeCardUseCase, observeWelcomeCardVisibilityUseCase)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides hideWelcomeCardUseCase: HideWelcomeCardUseCase,
            @Provides observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
        ): ForYouUiTestGraph
    }
}

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
