package dev.alvr.katana.data.preferences.session.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.dataStoreFile
import dev.alvr.katana.data.preferences.session.models.Session
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal actual fun dataStoreModule() = module {
    single {
        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = Session.serializer(get()),
                producePath = { androidApplication().dataStoreFile(DATASTORE_FILE).toOkioPath() },
            ),
            corruptionHandler = ReplaceFileCorruptionHandler { Session() },
        )
    }
}
