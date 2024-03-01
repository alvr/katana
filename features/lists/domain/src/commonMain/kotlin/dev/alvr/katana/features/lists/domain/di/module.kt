package dev.alvr.katana.features.lists.domain.di

import dev.alvr.katana.features.lists.domain.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.features.lists.domain.usecases.ObserveMangaListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val useCasesModule = module {
    factoryOf(::ObserveAnimeListUseCase)
    factoryOf(::ObserveMangaListUseCase)
    factoryOf(::UpdateListUseCase)
}

val domainListsModule = module {
    includes(useCasesModule)
}
