package dev.alvr.katana.data.remote.lists.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.lists.repositories.ListsRemoteRepositoryImpl
import dev.alvr.katana.domain.lists.repositories.ListsRepository

@Module
@InstallIn(SingletonComponent::class)
internal object ListsRemoteModule {
    @Provides
    fun provideListsRemoteRepository(impl: ListsRemoteRepositoryImpl): ListsRepository = impl
}
