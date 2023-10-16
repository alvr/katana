package dev.alvr.katana.ui.login.di

import dev.alvr.katana.ui.login.viewmodel.LoginViewModel
import org.koin.dsl.module

private val viewModelsModule = module {
    factory { params -> LoginViewModel(params[0], get(), get()) }
}

val uiLoginModule = module {
    includes(viewModelsModule)
}
