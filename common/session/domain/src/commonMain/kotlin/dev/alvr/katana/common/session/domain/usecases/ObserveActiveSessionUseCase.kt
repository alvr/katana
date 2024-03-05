package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveActiveSessionUseCase(
    private val repository: SessionRepository,
) : FlowEitherUseCase<Unit, Boolean>() {
    override fun createFlow(params: Unit) = repository.sessionActive
}
