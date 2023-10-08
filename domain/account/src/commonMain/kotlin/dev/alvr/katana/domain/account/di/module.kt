package dev.alvr.katana.domain.account.di

import dev.alvr.katana.domain.account.usecases.GetAccountInfoUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val useCasesModule = module {
    factoryOf(::GetAccountInfoUseCase)
}

val domainAccountModule = module {
    includes(useCasesModule)
}
