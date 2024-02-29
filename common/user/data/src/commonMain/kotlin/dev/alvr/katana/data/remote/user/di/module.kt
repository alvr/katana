package dev.alvr.katana.data.remote.user.di

import dev.alvr.katana.data.remote.user.managers.UserIdManagerImpl
import dev.alvr.katana.data.remote.user.repositories.UserRepositoryImpl
import dev.alvr.katana.data.remote.user.sources.id.UserIdRemoteSource
import dev.alvr.katana.data.remote.user.sources.id.UserIdRemoteSourceImpl
import dev.alvr.katana.data.remote.user.sources.info.UserInfoRemoteSource
import dev.alvr.katana.data.remote.user.sources.info.UserInfoRemoteSourceImpl
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
    singleOf(::UserIdRemoteSourceImpl).bind<UserIdRemoteSource>()
    singleOf(::UserInfoRemoteSourceImpl).bind<UserInfoRemoteSource>()
}

val dataRemoteUserModule = module {
    includes(managersModule, repositoriesModule, sourcesModule)
}
