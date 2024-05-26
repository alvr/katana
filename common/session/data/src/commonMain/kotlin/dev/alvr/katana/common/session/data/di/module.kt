package dev.alvr.katana.common.session.data.di

import dev.alvr.katana.common.session.data.repositories.SessionRepositoryImpl
import dev.alvr.katana.common.session.data.sources.SessionLocalSource
import dev.alvr.katana.common.session.data.sources.SessionLocalSourceImpl
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect fun dataStoreModule(): Module

private val repositoriesModule = module {
    singleOf(::SessionRepositoryImpl) bind SessionRepository::class
}

private val sourcesModule = module {
    singleOf(::SessionLocalSourceImpl) bind SessionLocalSource::class
}

val commonSessionDataModule = module {
    includes(dataStoreModule(), repositoriesModule, sourcesModule)
}

internal const val DATASTORE_FILE = "session.pb"
