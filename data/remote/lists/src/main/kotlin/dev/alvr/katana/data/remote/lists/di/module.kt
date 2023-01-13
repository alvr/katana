package dev.alvr.katana.data.remote.lists.di

import dev.alvr.katana.data.remote.lists.repositories.ListsRepositoryImpl
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSourceImpl
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val repositoriesModule = module {
    factoryOf(::ListsRepositoryImpl) bind ListsRepository::class
}

private val sourcesModule = module {
    factoryOf(::CommonListsRemoteSourceImpl) bind CommonListsRemoteSource::class
    singleOf(::AnimeListsRemoteSourceImpl) bind AnimeListsRemoteSource::class
    singleOf(::MangaListsRemoteSourceImpl) bind MangaListsRemoteSource::class
}

val listsDataRemoteModule = module {
    includes(repositoriesModule, sourcesModule)
}
