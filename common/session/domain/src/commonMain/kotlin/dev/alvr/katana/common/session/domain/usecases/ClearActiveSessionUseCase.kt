package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.domain.usecases.EitherUseCase

class ClearActiveSessionUseCase(
    private val repository: SessionRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.clearActiveSession()
}
