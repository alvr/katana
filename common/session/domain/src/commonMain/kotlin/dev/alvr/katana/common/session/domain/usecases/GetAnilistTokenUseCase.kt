package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.domain.usecases.OptionUseCase

class GetAnilistTokenUseCase(
    private val repository: SessionRepository,
) : OptionUseCase<Unit, AnilistToken> {
    override suspend fun invoke(params: Unit) = repository.getAnilistToken()
}
