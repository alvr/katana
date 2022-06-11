package dev.alvr.katana.data.remote.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.base.BuildConfig
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
internal object OkHttpClientModule {

    private const val CLIENT_TIMEOUT = 30L

    @Provides
    @Singleton
    @AnilistTokenInterceptor
    fun provideAnilistTokenInterceptor(
        getAnilistTokenUseCase: GetAnilistTokenUseCase,
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
        deleteAnilistTokenUseCase: DeleteAnilistTokenUseCase,
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
            .connectTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}
