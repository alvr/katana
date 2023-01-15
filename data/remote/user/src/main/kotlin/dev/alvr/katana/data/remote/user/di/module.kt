package dev.alvr.katana.data.remote.user.di

import dev.alvr.katana.data.remote.user.managers.UserIdManagerImpl
import dev.alvr.katana.data.remote.user.repositories.UserRepositoryImpl
import dev.alvr.katana.data.remote.user.sources.UserRemoteSource
import dev.alvr.katana.data.remote.user.sources.UserRemoteSourceImpl
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.repositories.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val managersModule = module {
    singleOf(::UserIdManagerImpl).bind<UserIdManager>()
}

private val repositoriesModule = module {
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
}

private val sourcesModule = module {
    singleOf(::UserRemoteSourceImpl).bind<UserRemoteSource>()
}

val userDataRemoteModule = module {
    includes(managersModule, repositoriesModule, sourcesModule)
}
