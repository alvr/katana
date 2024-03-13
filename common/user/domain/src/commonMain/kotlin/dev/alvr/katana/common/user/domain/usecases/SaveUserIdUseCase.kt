package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase

class SaveUserIdUseCase(
    dispatcher: KatanaDispatcher,
    private val repository: UserRepository,
) : EitherUseCase<Unit, Unit>(dispatcher) {
    override suspend fun run(params: Unit) = repository.saveUserId()
}
