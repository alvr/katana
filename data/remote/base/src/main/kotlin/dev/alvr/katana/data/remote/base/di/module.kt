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
import com.apollographql.apollo3.network.okHttpClient
import dev.alvr.katana.data.remote.base.BuildConfig
import dev.alvr.katana.data.remote.base.interceptors.ReloadInterceptor
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import io.sentry.apollo3.sentryTracing
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val apolloModule = module {
    single<NormalizedCacheFactory> {
        SqlNormalizedCacheFactory(
            androidApplication(),
            CACHE_DATABASE,
        )
    }

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

        val getAnilistTokenInterceptor = object : HttpInterceptor {
            val useCase = get<GetAnilistTokenUseCase>()

            override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
                request.newBuilder()
                    .addHeader("Authorization", "Bearer ${useCase().orNull()?.token}")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build().let { chain.proceed(it) }
        }

        val deleteAnilistTokenInterceptor = object : HttpInterceptor {
            val useCase = get<DeleteAnilistTokenUseCase>()

            override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
                chain.proceed(request).also { response ->
                    if (
                        response.statusCode == HttpURLConnection.HTTP_BAD_REQUEST ||
                        response.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED
                    ) {
                        useCase()
                    }
                }
        }

        ApolloClient.Builder()
            .serverUrl(ANILIST_BASE_URL)
            .addHttpInterceptor(getAnilistTokenInterceptor)
            .addHttpInterceptor(deleteAnilistTokenInterceptor)
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .okHttpClient(get())
            .sentryTracing()
            .normalizedCache(
                normalizedCacheFactory = get(),
                cacheKeyGenerator = cacheKeyGenerator,
                writeToCacheAsynchronously = true,
            )
            .build()
    }
}

private val interceptorsModule = module {
    factoryOf(::ReloadInterceptor) bind ApolloInterceptor::class
}

private val okhttpModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}

val baseDataRemoteModule = module {
    includes(apolloModule, interceptorsModule, okhttpModule)
}

private const val ANILIST_BASE_URL = "https://graphql.anilist.co"

private const val CACHE_DATABASE = "katana_data.db"
private const val CACHE_ID_KEY = "id"
private const val CACHE_TYPE_KEY = "__typename"

private const val CLIENT_TIMEOUT = 30L
