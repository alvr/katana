package dev.alvr.katana.features.lists.ui.di

import dev.alvr.katana.features.lists.ui.viewmodel.AnimeListsViewModel
import dev.alvr.katana.features.lists.ui.viewmodel.MangaListsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModelOf(::AnimeListsViewModel)
    viewModelOf(::MangaListsViewModel)
}

val featuresListsUiModule = module {
    includes(viewModelsModule)
}
