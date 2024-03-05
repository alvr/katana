package dev.alvr.katana.core.remote.di

import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.api.CacheKey
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGenerator
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGeneratorContext
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.http.LoggingInterceptor
import dev.alvr.katana.common.session.domain.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.common.session.domain.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.core.common.di.ApplicationScope
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.remote.KatanaBuildConfig
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface CoreRemoteComponent :
    CoreRemoteApolloClientComponent,
    CoreRemoteApolloInterceptorsComponent

interface CoreRemoteApolloClientComponent {
    @Provides
    @ApplicationScope
    fun provideApolloClient(
        interceptors: Set<HttpInterceptor>,
        normalizedCacheFactory: NormalizedCacheFactory,
    ): ApolloClient {
        val cacheKeyGenerator = object : CacheKeyGenerator {
            override fun cacheKeyForObject(
                obj: Map<String, Any?>,
                context: CacheKeyGeneratorContext,
            ): CacheKey? = if (
                obj[CACHE_ID_KEY] != null &&
                obj[CACHE_TYPE_KEY] != null
            ) {
                CacheKey(obj[CACHE_TYPE_KEY].toString(), obj[CACHE_ID_KEY].toString())
            } else {
                null
            }
        }

        return ApolloClient.Builder()
            .serverUrl(ANILIST_BASE_URL)
            .apply { interceptors.forEach { addHttpInterceptor(it) } }
            .sentryInterceptor()
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .normalizedCache(
                normalizedCacheFactory = normalizedCacheFactory,
                cacheKeyGenerator = cacheKeyGenerator,
                writeToCacheAsynchronously = true,
            )
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideApolloDatabase(): NormalizedCacheFactory =
        SqlNormalizedCacheFactory(CACHE_DATABASE)
}

interface CoreRemoteApolloInterceptorsComponent {
    @IntoSet
    @Provides
    @ApplicationScope
    fun provideGetTokenInterceptor(
        getAnilistTokenUseCase: GetAnilistTokenUseCase,
    ): HttpInterceptor = object : HttpInterceptor {
        override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
            request.newBuilder()
                .addHeader("Authorization", "Bearer ${getAnilistTokenUseCase().getOrNull()?.token}")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build().let { chain.proceed(it) }
    }

    @IntoSet
    @Provides
    @ApplicationScope
    fun provideDeleteTokenInterceptor(
        deleteAnilistTokenUseCase: DeleteAnilistTokenUseCase,
    ): HttpInterceptor = object : HttpInterceptor {
        override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
            chain.proceed(request).also { response ->
                if (
                    response.statusCode == HTTP_BAD_REQUEST ||
                    response.statusCode == HTTP_UNAUTHORIZED
                ) {
                    deleteAnilistTokenUseCase()
                }
            }
    }

    @IntoSet
    @Provides
    @ApplicationScope
    fun provideLoggingInterceptor(): HttpInterceptor = LoggingInterceptor(
        log = { Logger.i("ApolloLoggingInterceptor") { it } },
        level = if (KatanaBuildConfig.DEBUG) {
            LoggingInterceptor.Level.BODY
        } else {
            LoggingInterceptor.Level.NONE
        },
    )
}

private const val ANILIST_BASE_URL = "https://graphql.anilist.co"

private const val CACHE_DATABASE = "katana_data.db"
private const val CACHE_ID_KEY = "id"
private const val CACHE_TYPE_KEY = "__typename"

private const val HTTP_BAD_REQUEST = 400
private const val HTTP_UNAUTHORIZED = 401
