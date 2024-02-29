package dev.alvr.katana.ui.account.di

import dev.alvr.katana.ui.account.viewmodel.AccountViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val viewModelsModule = module {
    factoryOf(::AccountViewModel)
}

val uiAccountModule = module {
    includes(viewModelsModule)
}
