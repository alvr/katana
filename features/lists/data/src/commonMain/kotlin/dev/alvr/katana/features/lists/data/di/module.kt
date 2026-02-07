package dev.alvr.katana.features.lists.data.di

import dev.alvr.katana.features.lists.data.repositories.ListsRepositoryImpl
import dev.alvr.katana.features.lists.data.sources.ListsRemoteSource
import dev.alvr.katana.features.lists.data.sources.ListsRemoteSourceImpl
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val repositoriesModule = module { singleOf(::ListsRepositoryImpl) bind ListsRepository::class }

private val sourcesModule = module { singleOf(::ListsRemoteSourceImpl) bind ListsRemoteSource::class }

val featuresListsDataModule = module { includes(repositoriesModule, sourcesModule) }
