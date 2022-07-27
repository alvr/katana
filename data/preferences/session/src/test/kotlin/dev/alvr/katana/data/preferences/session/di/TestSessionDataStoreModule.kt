package dev.alvr.katana.data.preferences.session.di

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.alvr.katana.data.preferences.base.serializers.encoded
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.data.preferences.session.serializers.SessionSerializer
import javax.inject.Named
import javax.inject.Singleton
import kotlin.io.path.createTempFile
import kotlin.random.Random
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
    private const val CORRUPTED_DATASTORE_FILE = "err_test_session.pb"

    @Provides
    @Singleton
    @Named("dataStore")
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Session> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile(DATASTORE_FILE) },
            scope = TestScope(),
            serializer = SessionSerializer.encoded(),
        )

    @Provides
    @Singleton
    @Named("corruptedDataStore")
    fun provideSessionCorruptedDataStore(@ApplicationContext context: Context): DataStore<Session> {
        val createFile by lazy {
            createTempFile()
                .toFile()
                .absoluteFile
                .also { it.writeText(Base64.encodeToString(Random.nextBytes(64), Base64.NO_WRAP)) }
                .copyTo(context.dataStoreFile(CORRUPTED_DATASTORE_FILE))
        }

        return DataStoreFactory.create(
            produceFile = { createFile },
            scope = TestScope(),
            corruptionHandler = ReplaceFileCorruptionHandler {
                createFile.delete() // Delete to be able to create the file again
                Session(anilistToken = "recreated")
            },
            serializer = SessionSerializer.encoded(),
        )
    }
}
