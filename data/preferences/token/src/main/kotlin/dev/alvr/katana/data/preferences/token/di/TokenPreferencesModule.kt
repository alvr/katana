package dev.alvr.katana.data.preferences.token.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.preferences.base.encoded
import dev.alvr.katana.data.preferences.token.models.Token
import dev.alvr.katana.data.preferences.token.repositories.TokenPreferencesRepositoryImpl
import dev.alvr.katana.data.preferences.token.serializers.TokenSerializer
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi

@Module
@InstallIn(SingletonComponent::class)
internal object TokenPreferencesModule {

    private const val DATASTORE_FILE = "token.pb"

    @Provides
    @Singleton
    @ExperimentalSerializationApi
    fun provideTokenDataStore(@ApplicationContext context: Context): DataStore<Token> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile(DATASTORE_FILE) },
            serializer = TokenSerializer.encoded()
        )

    @Provides
    fun provideTokenPreferencesRepository(
        impl: TokenPreferencesRepositoryImpl
    ): TokenPreferencesRepository = impl
}
