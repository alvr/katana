package dev.alvr.katana.core.remote.di

import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.apollographql.apollo.network.http.LoggingInterceptor
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.cache.normalized.fetchPolicy
import dev.alvr.katana.common.session.domain.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.common.session.domain.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.core.common.KatanaBuildConfig
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.remote.cache.Cache.cache
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlin.jvm.JvmSuppressWildcards

@ContributesTo(AppScope::class)
interface ApolloProviders {

    @Multibinds fun httpInterceptors(): Set<HttpInterceptor>

    @IntoSet
    @Provides
    @SingleIn(AppScope::class)
    fun authInterceptor(
        getTokenUseCase: GetAnilistTokenUseCase,
        deleteTokenUseCase: DeleteAnilistTokenUseCase,
    ): HttpInterceptor =
        object : HttpInterceptor {
            override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
                val requestWithAuth =
                    request
                        .newBuilder()
                        .apply {
                            getTokenUseCase().getOrNull()?.token?.let { token ->
                                addHeader("Authorization", "Bearer $token")
                            }
                        }
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build()

                val response = chain.proceed(requestWithAuth)

                if (response.statusCode == HTTP_UNAUTHORIZED) {
                    deleteTokenUseCase()
                }

                return response
            }
        }

    @IntoSet
    @Provides
    @SingleIn(AppScope::class)
    fun loggingInterceptor(): HttpInterceptor =
        LoggingInterceptor(
            log = { Logger.i(tag = LogTag) { it } },
            level =
                if (KatanaBuildConfig.DEBUG) {
                    LoggingInterceptor.Level.BODY
                } else {
                    LoggingInterceptor.Level.NONE
                },
        )

    @Provides
    @SingleIn(AppScope::class)
    fun apollo(
        httpInterceptors: @JvmSuppressWildcards Set<HttpInterceptor>,
        normalizedCacheFactory: NormalizedCacheFactory,
    ): ApolloClient =
        ApolloClient.Builder()
            .serverUrl(ANILIST_BASE_URL)
            .apply {
                for (interceptor in httpInterceptors) {
                    addHttpInterceptor(interceptor)
                }
            }
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .cache(normalizedCacheFactory = normalizedCacheFactory, writeToCacheAsynchronously = true)
            .build()
}

private const val ANILIST_BASE_URL = "https://graphql.anilist.co"

private const val HTTP_UNAUTHORIZED = 401

private const val LogTag = "ApolloClient"
