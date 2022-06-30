package dev.alvr.katana.domain.session.usecases

import dev.alvr.katana.domain.base.usecases.FlowUseCase
import dev.alvr.katana.domain.session.repositories.SessionPreferencesRepository
import javax.inject.Inject

class ObserveSessionUseCase @Inject constructor(
    private val repository: SessionPreferencesRepository,
) : FlowUseCase<Unit, Boolean>() {
    override fun createFlow(params: Unit) = repository.isSessionExpired
}
