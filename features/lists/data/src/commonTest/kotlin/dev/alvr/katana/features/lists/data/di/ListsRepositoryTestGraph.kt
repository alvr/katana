package dev.alvr.katana.features.lists.data.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.lists.data.repositories.ListsRepositoryImpl
import dev.alvr.katana.features.lists.data.sources.ListsRemoteSource
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ListsRepositoryTestGraph : TestAppGraph {
    val listsRepository: ListsRepository

    @Provides fun listsRepository(remoteSource: ListsRemoteSource): ListsRepository = ListsRepositoryImpl(remoteSource)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides remoteSource: ListsRemoteSource): ListsRepositoryTestGraph
    }
}

internal fun createListsRepositoryTestGraph(remoteSource: ListsRemoteSource) =
    createGraphFactory<ListsRepositoryTestGraph.Factory>().create(remoteSource)
