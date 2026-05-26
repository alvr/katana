package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface SaveUserIdUseCase : KatanaEitherUseCase<Unit, Unit>

@ContributesBinding(AppScope::class, binding = binding<SaveUserIdUseCase>())
internal class SaveUserIdUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: UserRepository) :
    EitherUseCase<Unit, Unit>(dispatcher), SaveUserIdUseCase {
    override suspend fun run(params: Unit) = repository.saveUserId()
}
