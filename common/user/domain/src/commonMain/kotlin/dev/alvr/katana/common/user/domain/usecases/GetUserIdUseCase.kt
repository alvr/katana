package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface GetUserIdUseCase : KatanaEitherUseCase<Unit, UserId>

@ContributesBinding(AppScope::class, binding = binding<GetUserIdUseCase>())
internal class GetUserIdUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: UserRepository) :
    EitherUseCase<Unit, UserId>(dispatcher), GetUserIdUseCase {
    /**
 * Retrieves the current user's identifier from the repository.
 *
 * @return An `Either` result containing the `UserId` on success or an error on failure.
 */
override suspend fun run(params: Unit) = repository.getUserId()
}
