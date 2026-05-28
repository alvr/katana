package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface LogOutUseCase : KatanaEitherUseCase<Unit, Unit>

@ContributesBinding(AppScope::class, binding = binding<LogOutUseCase>())
internal class LogOutUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: SessionRepository) :
    EitherUseCase<Unit, Unit>(dispatcher), LogOutUseCase {
    /**
 * Performs logout by delegating to the session repository.
 *
 * @return `Unit` on success or an error wrapped in the use-case Either result.
 */
override suspend fun run(params: Unit) = repository.logout()
}
