package dev.alvr.katana.data.preferences.session.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.alvr.katana.data.preferences.base.serializers.encoded
import dev.alvr.katana.data.preferences.session.di.SessionDataStoreModule
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.data.preferences.session.serializers.SessionSerializer
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.serialization.ExperimentalSerializationApi

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SessionDataStoreModule::class],
)
@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalSerializationApi::class,
)
internal object TestSessionDataStoreModule {

    private const val DATASTORE_FILE = "test_session.pb"

    @Provides
    @Singleton
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Session> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile(DATASTORE_FILE) },
            scope = TestScope(),
            serializer = SessionSerializer.encoded(),
        )
}
