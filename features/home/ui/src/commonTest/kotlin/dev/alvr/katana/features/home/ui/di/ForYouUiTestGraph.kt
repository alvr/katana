package dev.alvr.katana.features.home.ui.di

import dev.alvr.katana.common.media.domain.usecases.ObserveMediaCollectionUseCase
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObservePopularMediaUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveTrendingMediaUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveUpcomingAnimeUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouEffect
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouState
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

private typealias ForYouVM = KatanaViewModel<ForYouState, ForYouEffect, ForYouIntent>

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ForYouUiTestGraph : TestAppGraph {
    val forYouViewModel: ForYouVM

    @Provides
    fun forYouViewModel(
        hideWelcomeCardUseCase: HideWelcomeCardUseCase,
        observePopularMediaUseCase: ObservePopularMediaUseCase,
        observeTrendingMediaUseCase: ObserveTrendingMediaUseCase,
        observeUpcomingAnimeUseCase: ObserveUpcomingAnimeUseCase,
        observeMediaCollectionUseCase: ObserveMediaCollectionUseCase,
        observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
    ): ForYouVM =
        ForYouViewModel(
            hideWelcomeCardUseCase = hideWelcomeCardUseCase,
            observePopularMediaUseCase = observePopularMediaUseCase,
            observeTrendingMediaUseCase = observeTrendingMediaUseCase,
            observeUpcomingAnimeUseCase = observeUpcomingAnimeUseCase,
            observeMediaCollectionUseCase = observeMediaCollectionUseCase,
            observeWelcomeCardVisibilityUseCase = observeWelcomeCardVisibilityUseCase,
        )

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides hideWelcomeCardUseCase: HideWelcomeCardUseCase,
            @Provides observePopularMediaUseCase: ObservePopularMediaUseCase,
            @Provides observeTrendingMediaUseCase: ObserveTrendingMediaUseCase,
            @Provides observeUpcomingAnimeUseCase: ObserveUpcomingAnimeUseCase,
            @Provides observeMediaCollectionUseCase: ObserveMediaCollectionUseCase,
            @Provides observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
        ): ForYouUiTestGraph
    }
}

internal fun createForYouUiTestGraph(
    hideWelcomeCardUseCase: HideWelcomeCardUseCase,
    observePopularMediaUseCase: ObservePopularMediaUseCase,
    observeTrendingMediaUseCase: ObserveTrendingMediaUseCase,
    observeUpcomingAnimeUseCase: ObserveUpcomingAnimeUseCase,
    observeMediaCollectionUseCase: ObserveMediaCollectionUseCase,
    observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
) =
    createGraphFactory<ForYouUiTestGraph.Factory>()
        .create(
            hideWelcomeCardUseCase = hideWelcomeCardUseCase,
            observePopularMediaUseCase = observePopularMediaUseCase,
            observeTrendingMediaUseCase = observeTrendingMediaUseCase,
            observeUpcomingAnimeUseCase = observeUpcomingAnimeUseCase,
            observeMediaCollectionUseCase = observeMediaCollectionUseCase,
            observeWelcomeCardVisibilityUseCase = observeWelcomeCardVisibilityUseCase,
        )
