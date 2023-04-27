package dev.alvr.katana.data.preferences.session.di

import dev.alvr.katana.data.preferences.session.repositories.SessionRepositoryImpl
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSource
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSourceImpl
import dev.alvr.katana.domain.session.repositories.SessionRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect fun dataStoreModule(): Module

private val repositoriesModule = module {
    singleOf(::SessionRepositoryImpl).bind<SessionRepository>()
}

private val sourcesModule = module {
    singleOf(::SessionLocalSourceImpl).bind<SessionLocalSource>()
}

val sessionDataPreferencesModule = module {
    includes(dataStoreModule(), repositoriesModule, sourcesModule)
}

internal const val DATASTORE_FILE = "session.pb"
