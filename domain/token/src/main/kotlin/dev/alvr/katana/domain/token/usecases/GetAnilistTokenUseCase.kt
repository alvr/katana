package dev.alvr.katana.domain.token.usecases

import dev.alvr.katana.domain.base.usecases.OptionUseCase
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import javax.inject.Inject

class GetAnilistTokenUseCase @Inject constructor(
    private val repository: TokenPreferencesRepository,
) : OptionUseCase<Unit, AnilistToken> {
    override suspend fun invoke(params: Unit) = repository.getAnilistToken()
}
