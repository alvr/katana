package dev.alvr.katana.features.lists.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.lists.data.sources.ListsRemoteSource
import dev.alvr.katana.features.lists.data.sources.ListsRemoteSourceImpl
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ListsRemoteSourceTestGraph : TestAppGraph {
    val listsRemoteSource: ListsRemoteSource

    @Provides
    fun listsRemoteSource(
        client: ApolloClient,
        userIdManager: UserIdManager,
        reloadInterceptor: ApolloInterceptor,
    ): ListsRemoteSource = ListsRemoteSourceImpl(client, userIdManager, reloadInterceptor)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides client: ApolloClient,
            @Provides userIdManager: UserIdManager,
            @Provides reloadInterceptor: ApolloInterceptor,
        ): ListsRemoteSourceTestGraph
    }
}

internal fun createListsRemoteSourceTestGraph(
    client: ApolloClient,
    userIdManager: UserIdManager,
    reloadInterceptor: ApolloInterceptor,
) = createGraphFactory<ListsRemoteSourceTestGraph.Factory>().create(client, userIdManager, reloadInterceptor)
