package dev.alvr.katana.features.home.data.di

import dev.alvr.katana.core.common.KatanaFilesPath
import dev.alvr.katana.core.preferences.di.store.KatanaStore
import dev.alvr.katana.core.preferences.di.store.katanaStoreOf
import dev.alvr.katana.features.home.data.datastore.homePreferencesDataStore
import dev.alvr.katana.features.home.data.entities.HomePreferences
import dev.alvr.katana.features.home.data.repositories.HomeRepositoryImpl
import dev.alvr.katana.features.home.data.sources.HomeLocalSource
import dev.alvr.katana.features.home.data.sources.HomeLocalSourceImpl
import dev.alvr.katana.features.home.data.sources.HomeRemoteSource
import dev.alvr.katana.features.home.data.sources.HomeRemoteSourceImpl
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val dataStoreModule = module {
    single<KatanaStore<HomePreferences>>(homePreferencesDataStore) {
        katanaStoreOf(get<KatanaFilesPath>(), "home", HomePreferences())
    }
}

private val repositoriesModule = module { singleOf(::HomeRepositoryImpl) bind HomeRepository::class }

private val sourcesModule = module {
    single<HomeLocalSource> { HomeLocalSourceImpl(store = get(homePreferencesDataStore)) }
    singleOf(::HomeRemoteSourceImpl) bind HomeRemoteSource::class
}

val featuresHomeDataModule = module { includes(dataStoreModule, repositoriesModule, sourcesModule) }
