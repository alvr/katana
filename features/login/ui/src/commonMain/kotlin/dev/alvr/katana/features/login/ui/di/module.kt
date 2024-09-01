package dev.alvr.katana.features.login.ui.di

import dev.alvr.katana.features.login.ui.viewmodel.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModel { params -> LoginViewModel(params[0], get(), get()) }
}

val featuresLoginUiModule = module {
    includes(viewModelsModule)
}
