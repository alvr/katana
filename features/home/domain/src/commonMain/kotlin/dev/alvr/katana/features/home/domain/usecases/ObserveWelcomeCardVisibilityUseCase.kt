package dev.alvr.katana.features.home.domain.usecases

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowEitherUseCase
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface ObserveWelcomeCardVisibilityUseCase : KatanaFlowEitherUseCase<Unit, Boolean>

@ContributesBinding(AppScope::class, binding = binding<ObserveWelcomeCardVisibilityUseCase>())
internal class ObserveWelcomeCardVisibilityUseCaseImpl(
    dispatcher: KatanaDispatcher,
    private val repository: HomeRepository,
) : FlowEitherUseCase<Unit, Boolean>(dispatcher), ObserveWelcomeCardVisibilityUseCase {
    override fun createFlow(params: Unit) = repository.welcomeCardVisible
}
