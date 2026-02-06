package dev.alvr.katana.features.home.domain.usecases

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.features.home.domain.repositories.HomeRepository

class ObserveWelcomeCardVisibilityUseCase(dispatcher: KatanaDispatcher, private val repository: HomeRepository) :
    FlowEitherUseCase<Unit, Boolean>(dispatcher) {
    override fun createFlow(params: Unit) = repository.welcomeCardVisible
}
