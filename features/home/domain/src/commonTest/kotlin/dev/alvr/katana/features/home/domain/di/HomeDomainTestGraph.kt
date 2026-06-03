package dev.alvr.katana.features.home.domain.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCaseImpl
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCaseImpl
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface HomeDomainTestGraph : TestAppGraph {
    val hideWelcomeCardUseCase: HideWelcomeCardUseCase
    val observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase

    @Provides
    fun provideHideWelcomeCardUseCase(
        dispatcher: KatanaDispatcher,
        repository: HomeRepository,
    ): HideWelcomeCardUseCase = HideWelcomeCardUseCaseImpl(dispatcher, repository)

    @Provides
    fun provideObserveWelcomeCardVisibilityUseCase(
        dispatcher: KatanaDispatcher,
        repository: HomeRepository,
    ): ObserveWelcomeCardVisibilityUseCase = ObserveWelcomeCardVisibilityUseCaseImpl(dispatcher, repository)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides homeRepository: HomeRepository): HomeDomainTestGraph
    }
}

internal fun createHomeDomainTestGraph(homeRepository: HomeRepository) =
    createGraphFactory<HomeDomainTestGraph.Factory>().create(homeRepository)
