package dev.alvr.katana.common.user.data.di

import dev.alvr.katana.common.user.data.managers.UserIdManagerImpl
import dev.alvr.katana.common.user.data.repositories.UserRepositoryImpl
import dev.alvr.katana.common.user.data.sources.id.UserIdRemoteSource
import dev.alvr.katana.common.user.data.sources.id.UserIdRemoteSourceImpl
import dev.alvr.katana.common.user.data.sources.info.UserInfoRemoteSource
import dev.alvr.katana.common.user.data.sources.info.UserInfoRemoteSourceImpl
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.common.user.domain.repositories.UserRepository
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
