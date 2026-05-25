package dev.alvr.katana.features.lists.domain.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.alvr.katana.features.lists.domain.usecases.ObserveListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ListsDomainTestGraph : TestAppGraph {
    val observeListUseCase: ObserveListUseCase
    val updateListUseCase: UpdateListUseCase

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides listsRepository: ListsRepository): ListsDomainTestGraph
    }
}

internal fun createListsDomainTestGraph(listsRepository: ListsRepository) =
    createGraphFactory<ListsDomainTestGraph.Factory>().create(listsRepository)
