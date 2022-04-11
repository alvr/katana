package dev.alvr.katana.domain.token.usecases

import dev.alvr.katana.domain.base.di.IoDispatcher
import dev.alvr.katana.domain.base.usecases.UseCase
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class GetAnilistTokenUseCase @Inject constructor(
    private val repository: TokenPreferencesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, AnilistToken?>(dispatcher) {
    override suspend fun doWork(params: Unit): AnilistToken? =
        repository.getAnilistToken()
}
