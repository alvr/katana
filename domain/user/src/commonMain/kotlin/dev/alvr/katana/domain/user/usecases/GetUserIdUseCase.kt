package dev.alvr.katana.domain.user.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.repositories.UserRepository

class GetUserIdUseCase(
    private val repository: UserRepository,
) : EitherUseCase<Unit, UserId> {
    override suspend fun invoke(params: Unit) = repository.getUserId()
}