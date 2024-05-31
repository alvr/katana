package dev.alvr.katana.features.explore.ui.di

import dev.alvr.katana.features.explore.ui.viewmodel.ExploreViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModelOf(::ExploreViewModel)
}

val featuresExploreUiModule = module {
    includes(viewModelsModule)
}
