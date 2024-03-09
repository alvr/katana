package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.OptionUseCase

class GetAnilistTokenUseCase(
    dispatcher: KatanaDispatcher,
    private val repository: SessionRepository,
) : OptionUseCase<Unit, AnilistToken>(dispatcher) {
    override suspend fun run(params: Unit) = repository.getAnilistToken()
}
