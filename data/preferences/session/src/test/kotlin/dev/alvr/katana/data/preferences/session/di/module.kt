package dev.alvr.katana.data.preferences.session.di

import android.util.Base64
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import dev.alvr.katana.data.preferences.base.di.baseDataPreferencesModule
import dev.alvr.katana.data.preferences.base.serializers.encoded
import dev.alvr.katana.data.preferences.base.serializers.encrypted
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.data.preferences.session.serializers.SessionSerializer
import kotlin.io.path.createTempFile
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DATASTORE = "dataStore"
private const val CORRUPTED_DATASTORE = "corruptedDataStore"
private const val DATASTORE_FILE = "test_session.pb"
private const val CORRUPTED_DATASTORE_FILE = "err_test_session.pb"

internal val dataStoreNamed = named(DATASTORE)
internal val corruptedDataStoreNamed = named(CORRUPTED_DATASTORE)

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalSerializationApi::class,
)
internal val dataStoreModule = module {
    includes(baseDataPreferencesModule)

    fun serializers(aead: Aead) = listOf(
        SessionSerializer.encoded(),
        SessionSerializer.encrypted(aead),
    )

    single(dataStoreNamed) {
        serializers(get()).mapIndexed { index, serializer ->
            DataStoreFactory.create(
                produceFile = { androidApplication().dataStoreFile("${DATASTORE_FILE}_$index") },
                scope = TestScope(),
                serializer = serializer,
            )
        }.toTypedArray()
    }

    single(corruptedDataStoreNamed) {
        serializers(get()).mapIndexed { index, serializer ->
            val createFile by lazy {
                createTempFile()
                    .toFile()
                    .absoluteFile
                    .apply { writeText(Base64.encodeToString(Random.nextBytes(64), Base64.NO_WRAP)) }
                    .copyTo(androidApplication().dataStoreFile("${CORRUPTED_DATASTORE_FILE}_$index"))
            }

            DataStoreFactory.create(
                produceFile = { createFile },
                scope = TestScope(),
                corruptionHandler = ReplaceFileCorruptionHandler {
                    createFile.delete() // Delete to be able to create the file again
                    Session(anilistToken = "recreated")
                },
                serializer = serializer,
            )
        }.toTypedArray()
    }
}
