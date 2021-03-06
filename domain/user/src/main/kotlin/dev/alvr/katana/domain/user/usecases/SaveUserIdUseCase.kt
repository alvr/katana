package dev.alvr.katana.domain.user.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.user.repositories.UserRemoteRepository
import javax.inject.Inject

class SaveUserIdUseCase @Inject constructor(
    private val repository: UserRemoteRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.saveUserId()
}
