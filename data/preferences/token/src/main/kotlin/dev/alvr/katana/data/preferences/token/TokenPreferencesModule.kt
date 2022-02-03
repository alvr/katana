package dev.alvr.katana.data.preferences.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.preferences.base.encoded
import kotlinx.serialization.ExperimentalSerializationApi

@Module
@InstallIn(SingletonComponent::class)
internal object TokenPreferencesModule {

    @Provides
    @ExperimentalSerializationApi
    fun provideTokenDataStore(@ApplicationContext context: Context): DataStore<Token> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile(DATASTORE_FILE) },
            corruptionHandler = ReplaceFileCorruptionHandler { Token() },
            serializer = TokenSerializer.encoded()
        )

    private const val DATASTORE_FILE = "token.pb"
}
