package dev.alvr.katana.data.remote.lists.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.remote.lists.repositories.ListsRepositoryImpl
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSourceImpl
import dev.alvr.katana.domain.lists.repositories.ListsRepository

@Module
@InstallIn(SingletonComponent::class)
internal sealed interface ListsBindingsModule {
    @Binds
    fun bindListsRepository(impl: ListsRepositoryImpl): ListsRepository

    @Binds
    fun bindAnimeListsRemoteSource(impl: AnimeListsRemoteSourceImpl): AnimeListsRemoteSource

    @Binds
    fun bindMangaListsRemoteSource(impl: MangaListsRemoteSourceImpl): MangaListsRemoteSource

    @Binds
    fun bindCommonListsRemoteSource(impl: CommonListsRemoteSourceImpl): CommonListsRemoteSource
}
