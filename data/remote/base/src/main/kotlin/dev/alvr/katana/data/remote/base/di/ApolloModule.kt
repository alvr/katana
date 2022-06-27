package dev.alvr.katana.data.remote.base.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.cache.normalized.api.CacheKey
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGenerator
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGeneratorContext
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.token.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import java.net.HttpURLConnection
import javax.inject.Singleton
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
internal object ApolloModule {

    private const val ANILIST_BASE_URL = "https://graphql.anilist.co"

    private const val CACHE_DATABASE = "katana_data.db"
    private const val CACHE_ID_KEY = "id"
    private const val CACHE_TYPE_KEY = "__typename"

    @Provides
    @Singleton
    fun provideApolloCache(
        @ApplicationContext context: Context,
    ): NormalizedCacheFactory = SqlNormalizedCacheFactory(context, CACHE_DATABASE)

    @Provides
    @Singleton
    @AnilistTokenInterceptor
    fun provideAnilistTokenInterceptor(
        getAnilistToken: GetAnilistTokenUseCase,
    ): HttpInterceptor = object : HttpInterceptor {
        override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
            request.newBuilder()
                .addHeader("Authorization", "Bearer ${getAnilistToken().orNull()?.token}")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build().let { chain.proceed(it) }
    }

    @Provides
    @Singleton
    @SessionInterceptor
    fun provideSessionInterceptor(
        deleteAnilistToken: DeleteAnilistTokenUseCase,
    ): HttpInterceptor = object : HttpInterceptor {
        override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain) =
            chain.proceed(request).also { response ->
                if (response.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    deleteAnilistToken()
                }
            }
    }

    @Provides
    @Singleton
    fun provideApolloClient(
        client: OkHttpClient,
        cache: NormalizedCacheFactory,
        @AnilistTokenInterceptor anilistTokenInterceptor: HttpInterceptor,
        @SessionInterceptor sessionInterceptor: HttpInterceptor,
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
            .addHttpInterceptor(anilistTokenInterceptor)
            .addHttpInterceptor(sessionInterceptor)
            .okHttpClient(client)
            .normalizedCache(
                normalizedCacheFactory = cache,
                cacheKeyGenerator = cacheKeyGenerator,
                writeToCacheAsynchronously = true,
            )
            .build()
    }
}
