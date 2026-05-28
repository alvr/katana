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

    /**
     * Provides a test binding for HideWelcomeCardUseCase backed by its concrete implementation.
     *
     * @param dispatcher Dispatcher used by the use case for coroutine execution.
     * @param repository Repository providing home-related data and persistence.
     * @return An instance of [HideWelcomeCardUseCase].
     */
    @Provides
    fun provideHideWelcomeCardUseCase(
        dispatcher: KatanaDispatcher,
        repository: HomeRepository,
    ): HideWelcomeCardUseCase = HideWelcomeCardUseCaseImpl(dispatcher, repository)

    /**
     * Creates an ObserveWelcomeCardVisibilityUseCase implementation backed by the HomeRepository.
     *
     * @return An ObserveWelcomeCardVisibilityUseCase that emits welcome-card visibility changes. 
     */
    @Provides
    fun provideObserveWelcomeCardVisibilityUseCase(
        dispatcher: KatanaDispatcher,
        repository: HomeRepository,
    ): ObserveWelcomeCardVisibilityUseCase = ObserveWelcomeCardVisibilityUseCaseImpl(dispatcher, repository)

    @DependencyGraph.Factory
    interface Factory {
        /**
 * Creates a HomeDomainTestGraph configured with the provided HomeRepository.
 *
 * @param homeRepository The HomeRepository instance to supply into the graph's bindings.
 * @return A HomeDomainTestGraph that uses the given HomeRepository.
 */
fun create(@Provides homeRepository: HomeRepository): HomeDomainTestGraph
    }
}

internal fun createHomeDomainTestGraph(homeRepository: HomeRepository) =
    createGraphFactory<HomeDomainTestGraph.Factory>().create(homeRepository)
