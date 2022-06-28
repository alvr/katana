package dev.alvr.katana.domain.token.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import javax.inject.Inject

class DeleteAnilistTokenUseCase @Inject constructor(
    private val repository: TokenPreferencesRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit) = repository.deleteAnilistToken()
}
