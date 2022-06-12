package dev.alvr.katana.data.remote.user.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.user.managers.UserIdManagerImpl
import dev.alvr.katana.data.remote.user.repositories.UserRemoteRepositoryImpl
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.repositories.UserRemoteRepository

@Module
@InstallIn(SingletonComponent::class)
internal sealed interface UserBindingsModule {
    @Binds
    fun bindUserRemoteRepository(impl: UserRemoteRepositoryImpl): UserRemoteRepository

    @Binds
    fun bindUserIdManager(impl: UserIdManagerImpl): UserIdManager
}
