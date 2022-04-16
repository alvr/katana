package dev.alvr.katana.domain.user.usecases

import dev.alvr.katana.domain.base.di.IoDispatcher
import dev.alvr.katana.domain.base.usecases.UseCase
import dev.alvr.katana.domain.user.repositories.UserRemoteRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class SaveUserIdUseCase @Inject constructor(
    private val repository: UserRemoteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun doWork(params: Unit) {
        repository.saveUserId()
    }
}
