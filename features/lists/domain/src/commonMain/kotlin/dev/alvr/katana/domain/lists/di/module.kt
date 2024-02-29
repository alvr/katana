package dev.alvr.katana.domain.lists.di

import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
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
