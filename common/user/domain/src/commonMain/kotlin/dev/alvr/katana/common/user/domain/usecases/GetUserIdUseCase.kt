package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.domain.usecases.EitherUseCase

class GetUserIdUseCase(
    private val repository: UserRepository,
) : EitherUseCase<Unit, UserId> {
    override suspend fun invoke(params: Unit) = repository.getUserId()
}
