package dev.alvr.katana.common.user.data.di

import dev.alvr.katana.common.user.data.managers.UserIdManagerImpl
import dev.alvr.katana.common.user.data.repositories.UserRepositoryImpl
import dev.alvr.katana.common.user.data.sources.UserLocalSource
import dev.alvr.katana.common.user.data.sources.UserLocalSourceImpl
import dev.alvr.katana.common.user.data.sources.UserRemoteSource
import dev.alvr.katana.common.user.data.sources.UserRemoteSourceImpl
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val managersModule = module { singleOf(::UserIdManagerImpl) bind UserIdManager::class }

private val repositoriesModule = module { singleOf(::UserRepositoryImpl) bind UserRepository::class }

private val sourcesModule = module {
    singleOf(::UserLocalSourceImpl) bind UserLocalSource::class
    singleOf(::UserRemoteSourceImpl) bind UserRemoteSource::class
}

val commonUserDataModule = module { includes(managersModule, repositoriesModule, sourcesModule) }
