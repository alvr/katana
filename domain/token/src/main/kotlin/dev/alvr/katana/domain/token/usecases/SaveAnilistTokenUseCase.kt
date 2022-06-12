package dev.alvr.katana.domain.token.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import javax.inject.Inject

class SaveAnilistTokenUseCase @Inject constructor(
    private val repository: TokenPreferencesRepository,
) : EitherUseCase<AnilistToken, Unit> {
    override suspend fun invoke(token: AnilistToken) = repository.saveAnilistToken(token)
}
