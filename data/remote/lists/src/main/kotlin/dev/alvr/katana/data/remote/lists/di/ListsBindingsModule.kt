package dev.alvr.katana.data.remote.lists.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.lists.repositories.ListsRemoteRepositoryImpl
import dev.alvr.katana.domain.lists.repositories.ListsRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ListsBindingsModule {
    @Binds
    abstract fun bindListsRemoteRepository(impl: ListsRemoteRepositoryImpl): ListsRepository
}
