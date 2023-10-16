package dev.alvr.katana.ui.lists.di

import dev.alvr.katana.ui.lists.viewmodel.AnimeListsViewModel
import dev.alvr.katana.ui.lists.viewmodel.MangaListsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val viewModelsModule = module {
    factoryOf(::AnimeListsViewModel)
    factoryOf(::MangaListsViewModel)
}

val uiListsModule = module {
    includes(viewModelsModule)
}
