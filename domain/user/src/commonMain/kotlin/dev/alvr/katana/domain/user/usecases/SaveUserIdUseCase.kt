package dev.alvr.katana.domain.user.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.user.repositories.UserRepository

class SaveUserIdUseCase(
    private val repository: UserRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.saveUserId()
}
