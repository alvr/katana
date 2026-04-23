package dev.alvr.katana.features.home.domain.usecases

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.zacsweers.metro.Inject

@Inject
class HideWelcomeCardUseCase
internal constructor(dispatcher: KatanaDispatcher, private val repository: HomeRepository) :
    EitherUseCase<Unit, Unit>(dispatcher) {
    override suspend fun run(params: Unit) = repository.hideWelcomeCard()
}
