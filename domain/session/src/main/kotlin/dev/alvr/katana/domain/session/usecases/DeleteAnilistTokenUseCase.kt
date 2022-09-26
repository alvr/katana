package dev.alvr.katana.domain.session.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.session.repositories.SessionRepository
import javax.inject.Inject

class DeleteAnilistTokenUseCase @Inject constructor(
    private val repository: SessionRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.deleteAnilistToken()
}
