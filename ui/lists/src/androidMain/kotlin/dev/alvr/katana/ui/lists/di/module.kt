package dev.alvr.katana.ui.lists.di

import dev.alvr.katana.ui.lists.viewmodel.AnimeListsViewModel
import dev.alvr.katana.ui.lists.viewmodel.MangaListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModelOf(::AnimeListsViewModel)
    viewModelOf(::MangaListsViewModel)
}

val uiListsModule = module {
    includes(viewModelsModule)
}
