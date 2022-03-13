package dev.alvr.katana.data.remote.user.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.user.sources.UserRemoteSourceImpl
import dev.alvr.katana.domain.user.sources.UserRemoteSource

@Module
@InstallIn(SingletonComponent::class)
internal object UserRemoteModule {
    @Provides
    fun provideUserRemoteSource(
        impl: UserRemoteSourceImpl
    ): UserRemoteSource = impl
}
