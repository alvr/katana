package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface ClearActiveSessionUseCase : KatanaEitherUseCase<Unit, Unit>

@ContributesBinding(AppScope::class, binding = binding<ClearActiveSessionUseCase>())
internal class ClearActiveSessionUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: SessionRepository) :
    EitherUseCase<Unit, Unit>(dispatcher), ClearActiveSessionUseCase {
    /**
 * Clears the currently active session.
 *
 * @return An `Either` representing the outcome: success with no payload, or a failure describing why the session could not be cleared.
 */
override suspend fun run(params: Unit) = repository.clearActiveSession()
}
