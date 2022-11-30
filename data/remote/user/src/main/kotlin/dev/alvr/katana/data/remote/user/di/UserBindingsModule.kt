package dev.alvr.katana.data.remote.user.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.user.managers.UserIdManagerImpl
import dev.alvr.katana.data.remote.user.repositories.UserRepositoryImpl
import dev.alvr.katana.data.remote.user.sources.UserRemoteSource
import dev.alvr.katana.data.remote.user.sources.UserRemoteSourceImpl
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.repositories.UserRepository

@Module
@InstallIn(SingletonComponent::class)
internal sealed interface UserBindingsModule {
    @Binds
    fun bindUserIdManager(impl: UserIdManagerImpl): UserIdManager

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindUserRemoteSource(impl: UserRemoteSourceImpl): UserRemoteSource
}
