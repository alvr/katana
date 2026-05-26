package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.KatanaOptionUseCase
import dev.alvr.katana.core.domain.usecases.OptionUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface GetAnilistTokenUseCase : KatanaOptionUseCase<Unit, AnilistToken>

@ContributesBinding(AppScope::class, binding = binding<GetAnilistTokenUseCase>())
internal class GetAnilistTokenUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: SessionRepository) :
    OptionUseCase<Unit, AnilistToken>(dispatcher), GetAnilistTokenUseCase {
    override suspend fun run(params: Unit) = repository.getAnilistToken()
}
