package dev.alvr.katana.features.home.ui.di

import dev.alvr.katana.features.home.ui.viewmodel.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModelOf(::HomeViewModel)
}

val featuresHomeUiModule = module {
    includes(viewModelsModule)
}
