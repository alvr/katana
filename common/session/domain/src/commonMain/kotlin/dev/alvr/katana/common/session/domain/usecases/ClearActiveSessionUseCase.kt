package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase

class ClearActiveSessionUseCase(
    dispatcher: KatanaDispatcher,
    private val repository: SessionRepository,
) : EitherUseCase<Unit, Unit>(dispatcher) {
    override suspend fun run(params: Unit) = repository.clearActiveSession()
}
