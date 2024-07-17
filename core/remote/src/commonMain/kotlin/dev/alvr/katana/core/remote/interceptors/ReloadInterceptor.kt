package dev.alvr.katana.core.remote.interceptors

import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.normalized.CacheAndNetworkInterceptor
import com.apollographql.apollo.cache.normalized.NetworkOnlyInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import kotlinx.coroutines.flow.Flow

internal class ReloadInterceptor : ApolloInterceptor {
    private var firstQuery = true

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain,
    ): Flow<ApolloResponse<D>> = if (firstQuery) {
        CacheAndNetworkInterceptor.intercept(request, chain).also { firstQuery = false }
    } else {
        NetworkOnlyInterceptor.intercept(request, chain)
    }
}
