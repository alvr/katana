package dev.alvr.katana.data.remote.base.di

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
import dev.alvr.katana.data.remote.base.interceptors.ReloadInterceptor
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import io.github.aakira.napier.Napier
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect fun ApolloClient.Builder.sentryInterceptor(): ApolloClient.Builder

private val getAnilistTokenInterceptor = named("getAnilistTokenInterceptor")
private val deleteAnilistTokenInterceptor = named("deleteAnilistTokenInterceptor")
private val loggingInterceptor = named("loggingInterceptor")

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
            .addHttpInterceptor(get(getAnilistTokenInterceptor))
            .addHttpInterceptor(get(deleteAnilistTokenInterceptor))
            .addHttpInterceptor(get(loggingInterceptor))
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
    single<HttpInterceptor>(getAnilistTokenInterceptor) {
        object : HttpInterceptor {
            val useCase = get<GetAnilistTokenUseCase>()

            override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
                request.newBuilder()
                    .addHeader("Authorization", "Bearer ${useCase().orNull()?.token}")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build().let { chain.proceed(it) }
        }
    }

    single<HttpInterceptor>(deleteAnilistTokenInterceptor) {
        object : HttpInterceptor {
            val useCase = get<DeleteAnilistTokenUseCase>()

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

    single<HttpInterceptor>(loggingInterceptor) {
        LoggingInterceptor(
            log = { Napier.i(tag = "ApolloLoggingInterceptor") { it } },
            level = LoggingInterceptor.Level.BODY,
        )
    }

    factoryOf(::ReloadInterceptor).bind<ApolloInterceptor>()
}

val baseDataRemoteModule = module {
    includes(apolloClientModule, apolloDatabaseModule, apolloInterceptorsModule)
}

private const val ANILIST_BASE_URL = "https://graphql.anilist.co"

private const val CACHE_DATABASE = "katana_data.db"
private const val CACHE_ID_KEY = "id"
private const val CACHE_TYPE_KEY = "__typename"

private const val HTTP_BAD_REQUEST = 400
private const val HTTP_UNAUTHORIZED = 401
