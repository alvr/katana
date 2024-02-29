package dev.alvr.katana.data.remote.lists.di

import dev.alvr.katana.data.remote.lists.repositories.ListsRepositoryImpl
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSourceImpl
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val repositoriesModule = module {
    singleOf(::ListsRepositoryImpl).bind<ListsRepository>()
}

private val sourcesModule = module {
    singleOf(::CommonListsRemoteSourceImpl).bind<CommonListsRemoteSource>()
    singleOf(::AnimeListsRemoteSourceImpl).bind<AnimeListsRemoteSource>()
    singleOf(::MangaListsRemoteSourceImpl).bind<MangaListsRemoteSource>()
}

val dataRemoteListsModule = module {
    includes(repositoriesModule, sourcesModule)
}
