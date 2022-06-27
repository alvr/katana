package dev.alvr.katana.data.remote.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.base.BuildConfig
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
internal object OkHttpClientModule {

    private const val CLIENT_TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}
