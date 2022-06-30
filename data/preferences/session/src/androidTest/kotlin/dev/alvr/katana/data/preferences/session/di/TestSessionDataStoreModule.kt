package dev.alvr.katana.data.preferences.session.di

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
@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
internal object TestSessionDataStoreModule {

    private const val DATASTORE_FILE = "test_session.pb"

    @Provides
    @Singleton
    fun provideScope(): TestScope = TestScope()

    @Provides
    @Singleton
    fun provideSessionDataStore(
        @ApplicationContext context: Context,
        scope: TestScope,
    ): DataStore<Session> = DataStoreFactory.create(
        produceFile = { context.dataStoreFile(DATASTORE_FILE) },
        scope = scope,
        serializer = SessionSerializer.encoded(),
    )
}
