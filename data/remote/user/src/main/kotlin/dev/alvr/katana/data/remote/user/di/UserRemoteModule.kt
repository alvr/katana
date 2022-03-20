package dev.alvr.katana.data.remote.user.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.user.managers.UserIdManagerImpl
import dev.alvr.katana.data.remote.user.repositories.UserRemoteRepositoryImpl
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.repositories.UserRemoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object UserRemoteModule {
    @Provides
    fun provideUserRemoteRepository(impl: UserRemoteRepositoryImpl): UserRemoteRepository = impl

    @Provides
    @Singleton
    fun provideUserIdManager(impl: UserIdManagerImpl): UserIdManager = impl
}
