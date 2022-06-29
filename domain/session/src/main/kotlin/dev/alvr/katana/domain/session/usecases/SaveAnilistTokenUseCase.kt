package dev.alvr.katana.domain.session.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.TokenPreferencesRepository
import javax.inject.Inject

class SaveAnilistTokenUseCase @Inject constructor(
    private val repository: TokenPreferencesRepository,
) : EitherUseCase<AnilistToken, Unit> {
    override suspend fun invoke(token: AnilistToken) = repository.saveAnilistToken(token)
}
