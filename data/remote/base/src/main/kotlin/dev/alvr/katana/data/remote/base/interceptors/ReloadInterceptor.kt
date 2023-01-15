package dev.alvr.katana.data.remote.base.interceptors

import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.cache.normalized.CacheAndNetworkInterceptor
import com.apollographql.apollo3.cache.normalized.NetworkOnlyInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
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
