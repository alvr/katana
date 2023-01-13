package dev.alvr.katana.data.preferences.session.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dev.alvr.katana.data.preferences.base.serializers.encrypted
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.data.preferences.session.repositories.SessionRepositoryImpl
import dev.alvr.katana.data.preferences.session.serializers.SessionSerializer
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSource
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSourceImpl
import dev.alvr.katana.domain.session.repositories.SessionRepository
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
private val dataStoreModule = module {
    single {
        DataStoreFactory.create(
            serializer = SessionSerializer.encrypted(get()),
            corruptionHandler = ReplaceFileCorruptionHandler { Session() },
            produceFile = { androidApplication().dataStoreFile(DATASTORE_FILE) },
        )
    }
}

private val repositoriesModule = module {
    factoryOf(::SessionRepositoryImpl) bind SessionRepository::class
}

private val sourcesModule = module {
    factoryOf(::SessionLocalSourceImpl) bind SessionLocalSource::class
}

val sessionDataPreferencesModule = module {
    includes(dataStoreModule, repositoriesModule, sourcesModule)
}

private const val DATASTORE_FILE = "session.pb"
