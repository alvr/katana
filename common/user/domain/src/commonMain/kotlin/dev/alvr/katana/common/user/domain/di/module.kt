package dev.alvr.katana.common.user.domain.di

import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val useCasesModule = module {
    factoryOf(::GetUserIdUseCase)
    factoryOf(::SaveUserIdUseCase)
    factoryOf(::ObserveUserInfoUseCase)
}

val domainUserModule = module {
    includes(useCasesModule)
}
