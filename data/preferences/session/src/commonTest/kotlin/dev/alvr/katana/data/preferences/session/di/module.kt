package dev.alvr.katana.data.preferences.session.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioStorage
import dev.alvr.katana.data.preferences.base.encrypt.PreferencesEncrypt
import dev.alvr.katana.data.preferences.session.models.Session
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okio.FileSystem
import okio.Path
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect val fileSystem: FileSystem
internal expect fun corruptionHandler(createFile: Path): ReplaceFileCorruptionHandler<Session>

private const val DATASTORE = "dataStore"
private const val CORRUPTED_DATASTORE = "corruptedDataStore"

private const val DATASTORE_FILE = "test_session.pb"
private const val CORRUPTED_DATASTORE_FILE = "err_test_session.pb"

internal val dataStoreNamed = named(DATASTORE)
internal val corruptedDataStoreNamed = named(CORRUPTED_DATASTORE)

private val dataStoreFile
    get() = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve(DATASTORE_FILE)
private val corruptedDataStoreFile
    get() = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve(CORRUPTED_DATASTORE_FILE)

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalEncodingApi::class)
internal fun testDataStoreModule() = module {
    singleOf(::FakePreferencesEncrypt) bind PreferencesEncrypt::class

    single(dataStoreNamed) {
        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = fileSystem,
                producePath = { dataStoreFile },
                serializer = Session.serializer(get()),
            ),
            scope = TestScope(UnconfinedTestDispatcher()),
        )
    }

    single(corruptedDataStoreNamed) {
        val createFile by lazy {
            fileSystem.write(corruptedDataStoreFile) {
                writeUtf8(Base64.encode(Random.nextBytes(32)))
            }
            corruptedDataStoreFile
        }

        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = fileSystem,
                producePath = { createFile },
                serializer = Session.serializer(get()),
            ),
            scope = TestScope(UnconfinedTestDispatcher()),
            corruptionHandler = corruptionHandler(createFile),
        )
    }
}

internal fun deleteDataStoreFiles() {
    with(fileSystem) {
        delete(path = dataStoreFile, mustExist = false)
        delete(path = corruptedDataStoreFile, mustExist = false)
    }
}
