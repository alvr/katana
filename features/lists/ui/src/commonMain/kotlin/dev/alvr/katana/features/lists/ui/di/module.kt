package dev.alvr.katana.features.lists.ui.di

import dev.alvr.katana.features.lists.ui.viewmodel.AnimeListsViewModel
import dev.alvr.katana.features.lists.ui.viewmodel.MangaListsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val viewModelsModule = module {
    factoryOf(::AnimeListsViewModel)
    factoryOf(::MangaListsViewModel)
}

val uiListsModule = module {
    includes(viewModelsModule)
}
