package dev.alvr.katana.features.account.ui.di

import dev.alvr.katana.features.account.ui.viewmodel.AccountViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModelOf(::AccountViewModel)
}

val featuresAccountUiModule = module {
    includes(viewModelsModule)
}
