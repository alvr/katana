package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase

class GetUserIdUseCase(
    dispatcher: KatanaDispatcher,
    private val repository: UserRepository,
) : EitherUseCase<Unit, UserId>(dispatcher) {
    override suspend fun run(params: Unit) = repository.getUserId()
}
