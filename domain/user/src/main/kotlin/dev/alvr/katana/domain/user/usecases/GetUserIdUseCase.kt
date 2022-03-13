package dev.alvr.katana.domain.user.usecases

import dev.alvr.katana.domain.base.IoDispatcher
import dev.alvr.katana.domain.base.UseCase
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.repositories.UserRemoteRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class GetUserIdUseCase @Inject constructor(
    private val repository: UserRemoteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, UserId>(dispatcher) {
    override suspend fun doWork(params: Unit): UserId =
        repository.getUserId()
}
