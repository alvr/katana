package dev.alvr.katana.common.media.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import dev.alvr.katana.common.media.data.sources.MediaCollectionRemoteSource
import dev.alvr.katana.common.media.data.sources.MediaCollectionRemoteSourceImpl
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface MediaCollectionRemoteSourceTestGraph : TestAppGraph {
    val mediaCollectionRemoteSource: MediaCollectionRemoteSource

    @Provides
    fun mediaCollectionRemoteSource(
        client: ApolloClient,
        getUserId: GetUserIdUseCase,
        reloadInterceptor: ApolloInterceptor,
    ): MediaCollectionRemoteSource = MediaCollectionRemoteSourceImpl(client, getUserId, reloadInterceptor)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides client: ApolloClient,
            @Provides getUserId: GetUserIdUseCase,
            @Provides reloadInterceptor: ApolloInterceptor,
        ): MediaCollectionRemoteSourceTestGraph
    }
}

internal fun createMediaCollectionRemoteSourceTestGraph(
    client: ApolloClient,
    getUserId: GetUserIdUseCase,
    reloadInterceptor: ApolloInterceptor,
) = createGraphFactory<MediaCollectionRemoteSourceTestGraph.Factory>().create(client, getUserId, reloadInterceptor)
