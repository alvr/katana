package dev.alvr.katana.domain.session.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.session.repositories.SessionPreferencesRepository
import javax.inject.Inject

class ClearActiveSessionUseCase @Inject constructor(
    private val repository: SessionPreferencesRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.clearActiveSession()
}
