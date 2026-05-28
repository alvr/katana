package dev.alvr.katana.features.lists.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
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

    /**
         * Provides a ListsRemoteSource backed by ListsRemoteSourceImpl using the supplied Apollo client, user-id use case, and reload interceptor.
         *
         * @param getUserId Use case that supplies the current user ID.
         * @return A `ListsRemoteSource` implementation backed by `ListsRemoteSourceImpl`.
         */
        @Provides
    fun listsRemoteSource(
        client: ApolloClient,
        getUserId: GetUserIdUseCase,
        reloadInterceptor: ApolloInterceptor,
    ): ListsRemoteSource =
        ListsRemoteSourceImpl(client = client, getUserId = getUserId, reloadInterceptor = reloadInterceptor)

    @DependencyGraph.Factory
    interface Factory {
        /**
         * Create a ListsRemoteSourceTestGraph configured with the provided test dependencies.
         *
         * @param client The ApolloClient used by the lists remote source.
         * @param userId Use case that provides the current user ID for requests.
         * @param reloadInterceptor ApolloInterceptor that handles reload/auth-related request interception.
         * @return A ListsRemoteSourceTestGraph instance configured with the given dependencies.
         */
        fun create(
            @Provides client: ApolloClient,
            @Provides userId: GetUserIdUseCase,
            @Provides reloadInterceptor: ApolloInterceptor,
        ): ListsRemoteSourceTestGraph
    }
}

/**
         * Creates a ListsRemoteSourceTestGraph configured with the provided Apollo client, user-id use case, and reload interceptor.
         *
         * @param client The ApolloClient used by the graph's network bindings.
         * @param getUserId The use case that provides the current user ID.
         * @param reloadInterceptor An ApolloInterceptor that triggers reload behavior for requests.
         * @return A configured ListsRemoteSourceTestGraph instance.
         */
        internal fun createListsRemoteSourceTestGraph(
    client: ApolloClient,
    getUserId: GetUserIdUseCase,
    reloadInterceptor: ApolloInterceptor,
) =
    createGraphFactory<ListsRemoteSourceTestGraph.Factory>()
        .create(client = client, userId = getUserId, reloadInterceptor = reloadInterceptor)
