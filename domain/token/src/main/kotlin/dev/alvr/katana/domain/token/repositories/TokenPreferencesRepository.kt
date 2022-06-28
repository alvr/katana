package dev.alvr.katana.domain.token.repositories

import arrow.core.Either
import arrow.core.Option
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.token.models.AnilistToken

interface TokenPreferencesRepository {
    suspend fun deleteAnilistToken(): Either<Failure, Unit>
    suspend fun getAnilistToken(): Option<AnilistToken>
    suspend fun saveAnilistToken(anilistToken: AnilistToken): Either<Failure, Unit>
}
