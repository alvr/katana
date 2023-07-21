package dev.alvr.katana.ui.login.di

import dev.alvr.katana.ui.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModelOf(::LoginViewModel)
}

val uiLoginModule = module {
    includes(viewModelsModule)
}
