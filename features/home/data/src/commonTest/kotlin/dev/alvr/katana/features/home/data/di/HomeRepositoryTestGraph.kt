package dev.alvr.katana.features.home.data.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.home.data.repositories.HomeRepositoryImpl
import dev.alvr.katana.features.home.data.sources.HomeLocalSource
import dev.alvr.katana.features.home.data.sources.HomeRemoteSource
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface HomeRepositoryTestGraph : TestAppGraph {
    val homeRepository: HomeRepository

    @Provides
    fun homeRepository(localSource: HomeLocalSource, remoteSource: HomeRemoteSource): HomeRepository =
        HomeRepositoryImpl(localSource, remoteSource)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides localSource: HomeLocalSource,
            @Provides remoteSource: HomeRemoteSource,
        ): HomeRepositoryTestGraph
    }
}

internal fun createHomeRepositoryTestGraph(localSource: HomeLocalSource, remoteSource: HomeRemoteSource) =
    createGraphFactory<HomeRepositoryTestGraph.Factory>().create(localSource, remoteSource)
