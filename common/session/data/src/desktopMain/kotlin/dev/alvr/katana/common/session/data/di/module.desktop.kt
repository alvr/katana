package dev.alvr.katana.common.session.data.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioStorage
import dev.alvr.katana.common.session.data.models.Session
import kotlin.io.path.Path
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun dataStoreModule()= module {
    single {
        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = Session.serializer(get()),
                producePath = { Path(System.getProperty("user.home"), DATASTORE_FILE).toOkioPath() },
            ),
            corruptionHandler = ReplaceFileCorruptionHandler { Session() },
        )
    }
}
