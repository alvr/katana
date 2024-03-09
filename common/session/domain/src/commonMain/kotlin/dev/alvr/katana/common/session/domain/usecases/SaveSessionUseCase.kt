package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase

class SaveSessionUseCase(
    dispatcher: KatanaDispatcher,
    private val repository: SessionRepository,
) : EitherUseCase<AnilistToken, Unit>(dispatcher) {
    override suspend fun run(params: AnilistToken) = repository.saveSession(params)
}
