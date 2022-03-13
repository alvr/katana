package dev.alvr.katana.data.remote.base

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
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
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import io.github.aakira.napier.BuildConfig
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

    private const val APOLLO_CACHE_SIZE = 10 * 1024 * 1024
    private const val APOLLO_CACHE_DATABASE = "apollo.db"

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
    fun provideOkHttpClient(
        @AnilistTokenInterceptor tokenInterceptor: Interceptor
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
            .connectTimeout(ANILIST_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ANILIST_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ANILIST_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloCache(
        @ApplicationContext context: Context
    ): NormalizedCacheFactory = MemoryCacheFactory(APOLLO_CACHE_SIZE)
        .chain(SqlNormalizedCacheFactory(context, APOLLO_CACHE_DATABASE))

    @Provides
    @Singleton
    fun provideApolloClient(
        client: OkHttpClient,
        cache: NormalizedCacheFactory,
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl(ANILIST_BASE_URL)
        .okHttpClient(client)
        .normalizedCache(cache)
        .build()
}
