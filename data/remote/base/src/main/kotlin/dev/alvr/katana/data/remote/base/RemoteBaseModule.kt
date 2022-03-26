package dev.alvr.katana.data.remote.base

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.CacheKey
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGenerator
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGeneratorContext
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.domain.base.sync
import dev.alvr.katana.domain.token.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import io.github.aakira.napier.BuildConfig
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteBaseModule {

    private const val ANILIST_BASE_URL = "https://graphql.anilist.co"
    private const val ANILIST_CLIENT_TIMEOUT = 30L

    private const val APOLLO_CACHE_DATABASE = "apollo.db"
    private const val APOLLO_CACHE_ID_KEY = "id"
    private const val APOLLO_CACHE_TYPE_KEY = "__typename"

    @Provides
    @Singleton
    @AnilistTokenInterceptor
    fun provideAnilistTokenInterceptor(
        getAnilistTokenUseCase: GetAnilistTokenUseCase
    ): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${getAnilistTokenUseCase.sync()?.token}")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()

        chain.proceed(request)
    }

    @Provides
    @Singleton
    @SessionInterceptor
    fun provideSessionInterceptor(
        deleteAnilistTokenUseCase: DeleteAnilistTokenUseCase
    ): Interceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            deleteAnilistTokenUseCase.sync()
        }

        response
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @AnilistTokenInterceptor tokenInterceptor: Interceptor,
        @SessionInterceptor sessionInterceptor: Interceptor,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(sessionInterceptor)
            .connectTimeout(ANILIST_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ANILIST_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ANILIST_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloCache(
        @ApplicationContext context: Context
    ): NormalizedCacheFactory = SqlNormalizedCacheFactory(context, APOLLO_CACHE_DATABASE)

    @Provides
    @Singleton
    fun provideApolloClient(
        client: OkHttpClient,
        cache: NormalizedCacheFactory,
    ): ApolloClient {
        val cacheKeyGenerator = object : CacheKeyGenerator {
            override fun cacheKeyForObject(
                obj: Map<String, Any?>,
                context: CacheKeyGeneratorContext
            ): CacheKey? = if (
                obj[APOLLO_CACHE_ID_KEY] != null &&
                obj[APOLLO_CACHE_TYPE_KEY] != null
            ) {
                CacheKey(obj[APOLLO_CACHE_TYPE_KEY].toString(), obj[APOLLO_CACHE_ID_KEY].toString())
            } else {
                null
            }
        }

        return ApolloClient.Builder()
            .serverUrl(ANILIST_BASE_URL)
            .okHttpClient(client)
            .normalizedCache(
                normalizedCacheFactory = cache,
                cacheKeyGenerator = cacheKeyGenerator,
                writeToCacheAsynchronously = true,
            )
            .build()
    }
}
