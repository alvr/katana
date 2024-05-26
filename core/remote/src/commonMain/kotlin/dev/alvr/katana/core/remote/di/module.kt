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
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.http.LoggingInterceptor
import dev.alvr.katana.common.session.domain.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.common.session.domain.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.remote.KatanaBuildConfig
import dev.alvr.katana.core.remote.interceptors.ReloadInterceptor
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect fun ApolloClient.Builder.sentryInterceptor(): ApolloClient.Builder

private enum class Interceptor {
    GET_TOKEN,
    DELETE_TOKEN,
    LOGGING,
}

private val apolloClientModule = module {
    single {
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

        ApolloClient.Builder()
            .serverUrl(ANILIST_BASE_URL)
            .addHttpInterceptor(get(named(Interceptor.GET_TOKEN)))
            .addHttpInterceptor(get(named(Interceptor.DELETE_TOKEN)))
            .addHttpInterceptor(get(named(Interceptor.LOGGING)))
            .sentryInterceptor()
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .normalizedCache(
                normalizedCacheFactory = get(),
                cacheKeyGenerator = cacheKeyGenerator,
                writeToCacheAsynchronously = true,
            )
            .build()
    }
}

private val apolloDatabaseModule: Module = module {
    single<NormalizedCacheFactory> { SqlNormalizedCacheFactory(CACHE_DATABASE) }
}

private val apolloInterceptorsModule = module {
    single<HttpInterceptor>(named(Interceptor.GET_TOKEN)) {
        val useCase = get<GetAnilistTokenUseCase>()

        object : HttpInterceptor {
            override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
                request.newBuilder()
                    .addHeader("Authorization", "Bearer ${useCase().getOrNull()?.token}")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build().let { chain.proceed(it) }
        }
    }

    single<HttpInterceptor>(named(Interceptor.DELETE_TOKEN)) {
        val useCase = get<DeleteAnilistTokenUseCase>()

        object : HttpInterceptor {
            override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
                chain.proceed(request).also { response ->
                    if (
                        response.statusCode == HTTP_BAD_REQUEST ||
                        response.statusCode == HTTP_UNAUTHORIZED
                    ) {
                        useCase()
                    }
                }
        }
    }

    single<HttpInterceptor>(named(Interceptor.LOGGING)) {
        LoggingInterceptor(
            log = { Logger.i("ApolloLoggingInterceptor") { it } },
            level = if (KatanaBuildConfig.DEBUG) {
                LoggingInterceptor.Level.BODY
            } else {
                LoggingInterceptor.Level.NONE
            },
        )
    }

    factoryOf(::ReloadInterceptor) bind ApolloInterceptor::class
}

val coreRemoteModule = module {
    includes(apolloClientModule, apolloDatabaseModule, apolloInterceptorsModule)
}

private const val ANILIST_BASE_URL = "https://graphql.anilist.co"

private const val CACHE_DATABASE = "katana_data.db"
private const val CACHE_ID_KEY = "id"
private const val CACHE_TYPE_KEY = "__typename"

private const val HTTP_BAD_REQUEST = 400
private const val HTTP_UNAUTHORIZED = 401
