package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.domain.usecases.EitherUseCase

class SaveUserIdUseCase(
    private val repository: UserRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.saveUserId()
}
