package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface ObserveActiveSessionUseCase : KatanaFlowEitherUseCase<Unit, Boolean>

@ContributesBinding(AppScope::class, binding = binding<ObserveActiveSessionUseCase>())
internal class ObserveActiveSessionUseCaseImpl(
    dispatcher: KatanaDispatcher,
    private val repository: SessionRepository,
) : FlowEitherUseCase<Unit, Boolean>(dispatcher), ObserveActiveSessionUseCase {
    /**
 * Provides the flow that emits whether an active session exists.
 *
 * @return A flow emitting `Either` results whose success value is a `Boolean`: `true` when an active session exists, `false` otherwise.
 */
override fun createFlow(params: Unit) = repository.sessionActive
}
