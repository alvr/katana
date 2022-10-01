package dev.alvr.katana.data.remote.lists.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.lists.repositories.ListsRepositoryImpl
import dev.alvr.katana.domain.lists.repositories.ListsRepository

@Module
@InstallIn(SingletonComponent::class)
internal sealed interface ListsBindingsModule {
    @Binds
    fun bindListsRepository(impl: ListsRepositoryImpl): ListsRepository
}
