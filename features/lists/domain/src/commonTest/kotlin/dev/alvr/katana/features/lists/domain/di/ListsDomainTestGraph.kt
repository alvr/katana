package dev.alvr.katana.features.lists.domain.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.alvr.katana.features.lists.domain.usecases.ObserveListUseCase
import dev.alvr.katana.features.lists.domain.usecases.ObserveListUseCaseImpl
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCaseImpl
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ListsDomainTestGraph : TestAppGraph {
    val observeListUseCase: ObserveListUseCase
    val updateListUseCase: UpdateListUseCase

    /**
         * Provides an `ObserveListUseCase` implementation using the supplied dispatcher and repository.
         *
         * @param dispatcher Dispatcher used to execute use case work.
         * @param repository Repository that exposes list data and updates.
         * @return An `ObserveListUseCase` that observes list changes via the provided dispatcher and repository.
         */
        @Provides
    fun provideObserveListUseCase(dispatcher: KatanaDispatcher, repository: ListsRepository): ObserveListUseCase =
        ObserveListUseCaseImpl(dispatcher, repository)

    /**
         * Provides an UpdateListUseCase using the given dispatcher and lists repository.
         *
         * @return An UpdateListUseCase that performs list updates with the provided dispatcher and repository.
         */
        @Provides
    fun provideUpdateListUseCase(dispatcher: KatanaDispatcher, repository: ListsRepository): UpdateListUseCase =
        UpdateListUseCaseImpl(dispatcher, repository)

    @DependencyGraph.Factory
    interface Factory {
        /**
 * Creates a ListsDomainTestGraph configured with the provided repository.
 *
 * @param listsRepository Repository instance supplied to the graph for use case construction.
 * @return A configured ListsDomainTestGraph.
 */
fun create(@Provides listsRepository: ListsRepository): ListsDomainTestGraph
    }
}

internal fun createListsDomainTestGraph(listsRepository: ListsRepository) =
    createGraphFactory<ListsDomainTestGraph.Factory>().create(listsRepository)
