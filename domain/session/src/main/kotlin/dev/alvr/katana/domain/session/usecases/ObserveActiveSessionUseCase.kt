package dev.alvr.katana.domain.session.usecases

import dev.alvr.katana.domain.base.usecases.FlowUseCase
import dev.alvr.katana.domain.session.repositories.SessionRepository
import javax.inject.Inject

class ObserveActiveSessionUseCase @Inject constructor(
    private val repository: SessionRepository,
) : FlowUseCase<Unit, Boolean>() {
    override fun createFlow(params: Unit) = repository.sessionActive
}
