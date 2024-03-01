package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.domain.usecases.EitherUseCase

class SaveSessionUseCase(
    private val repository: SessionRepository,
) : EitherUseCase<AnilistToken, Unit> {
    override suspend fun invoke(token: AnilistToken) = repository.saveSession(token)
}
