package dev.alvr.katana.domain.session.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.session.repositories.SessionRepository

class LogOutUseCase(
    private val repository: SessionRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.logout()
}
