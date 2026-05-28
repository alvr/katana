package dev.alvr.katana.features.home.domain.usecases

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface HideWelcomeCardUseCase : KatanaEitherUseCase<Unit, Unit>

@ContributesBinding(AppScope::class, binding = binding<HideWelcomeCardUseCase>())
internal class HideWelcomeCardUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: HomeRepository) :
    EitherUseCase<Unit, Unit>(dispatcher), HideWelcomeCardUseCase {
    /**
 * Hides the welcome card.
 *
 * Ignores the `params` argument.
 *
 * @return `Either` with `Right(Unit)` on success or `Left` containing an error on failure.
 */
override suspend fun run(params: Unit) = repository.hideWelcomeCard()
}
