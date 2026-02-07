package dev.alvr.katana.features.home.domain.di

import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val useCasesModule = module {
    factoryOf(::HideWelcomeCardUseCase)
    factoryOf(::ObserveWelcomeCardVisibilityUseCase)
}

val featuresHomeDomainModule = module { includes(useCasesModule) }
