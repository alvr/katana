package dev.alvr.katana.features.home.data.di

import com.apollographql.apollo.ApolloClient
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

    @Provides fun homeRemoteSource(client: ApolloClient): HomeRemoteSource = HomeRemoteSourceImpl(client)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides client: ApolloClient): HomeRemoteSourceTestGraph
    }
}

internal fun createHomeRemoteSourceTestGraph(client: ApolloClient) =
    createGraphFactory<HomeRemoteSourceTestGraph.Factory>().create(client = client)
