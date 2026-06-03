package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface DeleteAnilistTokenUseCase : KatanaEitherUseCase<Unit, Unit>

@ContributesBinding(AppScope::class, binding = binding<DeleteAnilistTokenUseCase>())
internal class DeleteAnilistTokenUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: SessionRepository) :
    EitherUseCase<Unit, Unit>(dispatcher), DeleteAnilistTokenUseCase {
    override suspend fun run(params: Unit) = repository.deleteAnilistToken()
}
