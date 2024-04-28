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
    singleOf(::UserIdManagerImpl) bind UserIdManager::class
}

private val repositoriesModule = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}

private val sourcesModule = module {
    singleOf(::UserIdRemoteSourceImpl) bind UserIdRemoteSource::class
    singleOf(::UserInfoRemoteSourceImpl) bind UserInfoRemoteSource::class
}

val commonUserDataModule = module {
    includes(managersModule, repositoriesModule, sourcesModule)
}
