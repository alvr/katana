package dev.alvr.katana.domain.session.usecases

import dev.alvr.katana.domain.base.usecases.OptionUseCase
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionPreferencesRepository
import javax.inject.Inject

class GetAnilistTokenUseCase @Inject constructor(
    private val repository: SessionPreferencesRepository,
) : OptionUseCase<Unit, AnilistToken> {
    override suspend fun invoke(params: Unit) = repository.getAnilistToken()
}
