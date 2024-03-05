package dev.alvr.katana.common.session.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioStorage
import dev.alvr.katana.common.session.data.models.Session
import dev.alvr.katana.core.common.di.ApplicationScope
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSHomeDirectory

actual interface PlatformCommonSessionDataComponent {

    @Provides
    @ApplicationScope
    fun provideDataStore(
        encrypt: PreferencesEncrypt,
    ): DataStore<Session> = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = Session.serializer(encrypt),
            producePath = { NSHomeDirectory().toPath().resolve(DATASTORE_FILE) },
        ),
        corruptionHandler = ReplaceFileCorruptionHandler { Session() },
        migrations = emptyList(),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    )
}
