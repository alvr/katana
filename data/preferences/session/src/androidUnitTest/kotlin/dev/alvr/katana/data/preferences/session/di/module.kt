package dev.alvr.katana.data.preferences.session.di

import android.util.Base64
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.dataStoreFile
import dev.alvr.katana.data.preferences.base.di.baseDataPreferencesModule
import dev.alvr.katana.data.preferences.base.securer.PreferencesEncrypt
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.domain.session.models.AnilistToken
import kotlin.io.path.createTempFile
import kotlin.random.Random
import kotlinx.coroutines.test.TestScope
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.fakefilesystem.FakeFileSystem
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DATASTORE = "dataStore"
private const val CORRUPTED_DATASTORE = "corruptedDataStore"
private const val DATASTORE_FILE = "test_session.pb"
private const val CORRUPTED_DATASTORE_FILE = "err_test_session.pb"

internal val dataStoreNamed = named(DATASTORE)
internal val corruptedDataStoreNamed = named(CORRUPTED_DATASTORE)

internal val dataStoreModule = module {
    includes(baseDataPreferencesModule)

    fun serializers(securer: PreferencesEncrypt) = listOf(
        Session.serializer(securer),
    )

    single(dataStoreNamed) {
        serializers(get()).mapIndexed { index, serializer ->
            DataStoreFactory.create(
                storage = OkioStorage(
                    fileSystem = FileSystem.SYSTEM,
                    producePath = {
                        androidApplication().dataStoreFile("${DATASTORE_FILE}_$index").toOkioPath()
                    },
                    serializer = serializer,
                ),
                scope = TestScope(),
            )
        }.toTypedArray()
    }

    single(corruptedDataStoreNamed) {
        serializers(get()).mapIndexed { index, serializer ->
            val fileSystem = FakeFileSystem()

            val createFile by lazy {
                createTempFile()
                    .toFile()
                    .absoluteFile
                    .apply {
                        writeText(Base64.encodeToString(Random.nextBytes(64), Base64.NO_WRAP))
                        copyTo(androidApplication().dataStoreFile("${CORRUPTED_DATASTORE_FILE}_$index"))
                    }
                    .toOkioPath()
            }

            DataStoreFactory.create(
                storage = OkioStorage(
                    fileSystem = FileSystem.SYSTEM,
                    producePath = { createFile },
                    serializer = serializer,
                ),
                scope = TestScope(),
                corruptionHandler = ReplaceFileCorruptionHandler {
                    fileSystem.delete(createFile) // Delete to be able to create the file again
                    Session(anilistToken = AnilistToken("recreated"))
                },
            )
        }.toTypedArray()
    }
}
