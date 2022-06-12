package dev.alvr.katana.domain.user.usecases

import dev.alvr.katana.domain.base.usecases.UseCase
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.repositories.UserRemoteRepository
import javax.inject.Inject
import javax.inject.Singleton

class GetUserIdUseCase @Inject constructor(
    private val repository: UserRemoteRepository,
) : UseCase<Unit, UserId> {
    override suspend fun invoke(params: Unit) = repository.getUserId()
}
