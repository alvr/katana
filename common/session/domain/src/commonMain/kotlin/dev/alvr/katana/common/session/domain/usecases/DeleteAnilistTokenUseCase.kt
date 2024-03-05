package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import me.tatarka.inject.annotations.Inject

@Inject
class DeleteAnilistTokenUseCase(
    private val repository: SessionRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.deleteAnilistToken()
}
