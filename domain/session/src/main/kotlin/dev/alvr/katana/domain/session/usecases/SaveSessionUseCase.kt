package dev.alvr.katana.domain.session.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository

class SaveSessionUseCase(
    private val repository: SessionRepository,
) : EitherUseCase<AnilistToken, Unit> {
    override suspend fun invoke(token: AnilistToken) = repository.saveSession(token)
}
