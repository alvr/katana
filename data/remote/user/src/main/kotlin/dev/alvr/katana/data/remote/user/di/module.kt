package dev.alvr.katana.data.remote.user.di

import dev.alvr.katana.data.remote.user.managers.UserIdManagerImpl
import dev.alvr.katana.data.remote.user.repositories.UserRepositoryImpl
import dev.alvr.katana.data.remote.user.sources.UserRemoteSource
import dev.alvr.katana.data.remote.user.sources.UserRemoteSourceImpl
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.repositories.UserRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val managersModule = module {
    singleOf(::UserIdManagerImpl) bind UserIdManager::class
}

private val repositoriesModule = module {
    factoryOf(::UserRepositoryImpl) bind UserRepository::class
}

private val sourcesModule = module {
    factoryOf(::UserRemoteSourceImpl) bind UserRemoteSource::class
}

val userDataRemoteModule = module {
    includes(managersModule, repositoriesModule, sourcesModule)
}
