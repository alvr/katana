package dev.alvr.katana.features.home.data.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.home.data.sources.HomeRemoteSource
import dev.alvr.katana.features.home.data.sources.HomeRemoteSourceImpl
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface HomeRemoteSourceTestGraph : TestAppGraph {
    val homeRemoteSource: HomeRemoteSource

    @Provides fun homeRemoteSource(): HomeRemoteSource = HomeRemoteSourceImpl()

    @DependencyGraph.Factory
    interface Factory {
        fun create(): HomeRemoteSourceTestGraph
    }
}

internal fun createHomeRemoteSourceTestGraph() = createGraphFactory<HomeRemoteSourceTestGraph.Factory>().create()
