package dev.alvr.katana.core.remote.interceptors

import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import com.apollographql.cache.normalized.CacheAndNetworkInterceptor
import com.apollographql.cache.normalized.NetworkFirstInterceptor
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.flow.Flow

internal class ReloadInterceptor : ApolloInterceptor {
    private val firstQuery = atomic(true)

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain,
    ): Flow<ApolloResponse<D>> =
        if (firstQuery.compareAndSet(expect = true, update = false)) {
            CacheAndNetworkInterceptor.intercept(request, chain)
        } else {
            NetworkFirstInterceptor.intercept(request, chain)
        }
}
