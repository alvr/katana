package dev.alvr.katana.features.login.ui.di

import dev.alvr.katana.features.login.ui.viewmodel.LoginViewModel
import org.koin.dsl.module

private val viewModelsModule = module {
    factory { params -> LoginViewModel(params[0], get(), get()) }
}

val uiLoginModule = module {
    includes(viewModelsModule)
}
