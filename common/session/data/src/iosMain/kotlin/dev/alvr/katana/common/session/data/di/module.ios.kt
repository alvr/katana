package dev.alvr.katana.common.session.data.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioStorage
import dev.alvr.katana.common.session.data.models.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

internal actual fun dataStoreModule() = module {
    single {
        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = Session.serializer(get()),
                producePath = { NSHomeDirectory().toPath().resolve(DATASTORE_FILE) },
            ),
            corruptionHandler = ReplaceFileCorruptionHandler { Session() },
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )
    }
}
