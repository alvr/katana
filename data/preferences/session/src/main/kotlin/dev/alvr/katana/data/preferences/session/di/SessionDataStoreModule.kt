package dev.alvr.katana.data.preferences.session.di

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
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.data.preferences.session.serializers.SessionSerializer
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi

@Module
@InstallIn(SingletonComponent::class)
internal object SessionDataStoreModule {

    private const val DATASTORE_FILE = "session.pb"

    @Provides
    @Singleton
    @ExperimentalSerializationApi
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Session> =
        DataStoreFactory.create(
            serializer = SessionSerializer.encoded(),
            corruptionHandler = ReplaceFileCorruptionHandler { Session() },
            produceFile = { context.dataStoreFile(DATASTORE_FILE) },
        )
}
