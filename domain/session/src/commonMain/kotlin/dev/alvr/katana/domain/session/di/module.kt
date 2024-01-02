package dev.alvr.katana.domain.session.di

import dev.alvr.katana.domain.session.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.domain.session.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.domain.session.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.domain.session.usecases.LogOutUseCase
import dev.alvr.katana.domain.session.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.domain.session.usecases.SaveSessionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val useCasesModule = module {
    factoryOf(::ClearActiveSessionUseCase)
    factoryOf(::DeleteAnilistTokenUseCase)
    factoryOf(::GetAnilistTokenUseCase)
    factoryOf(::LogOutUseCase)
    factoryOf(::ObserveActiveSessionUseCase)
    factoryOf(::SaveSessionUseCase)
}

val domainSessionModule = module {
    includes(useCasesModule)
}
