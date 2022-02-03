package dev.alvr.katana.data.remote.base

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteBaseModule {

    @Provides
    @Singleton
    @AnilistTokenInterceptor
    fun provideAnilistTokenInterceptor(
        @Named("anilistToken") token: String?
    ): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
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
            .addNetworkInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(
        client: OkHttpClient
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl(BASE_URL)
        .okHttpClient(client)
        .build()

    private const val BASE_URL = "https://graphql.anilist.co"
    private const val CLIENT_TIMEOUT = 30L
}
