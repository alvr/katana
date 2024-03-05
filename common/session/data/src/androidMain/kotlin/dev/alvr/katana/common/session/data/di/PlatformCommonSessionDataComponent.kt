package dev.alvr.katana.common.session.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.dataStoreFile
import dev.alvr.katana.common.session.data.models.Session
import dev.alvr.katana.core.common.di.ApplicationScope
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import me.tatarka.inject.annotations.Provides
import okio.FileSystem
import okio.Path.Companion.toOkioPath

actual interface PlatformCommonSessionDataComponent {

    @Provides
    @ApplicationScope
    fun provideDataStore(
        context: Context,
        encrypt: PreferencesEncrypt,
    ): DataStore<Session> = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = Session.serializer(encrypt),
            producePath = { context.dataStoreFile(DATASTORE_FILE).toOkioPath() },
        ),
        corruptionHandler = ReplaceFileCorruptionHandler { Session() },
    )
}
