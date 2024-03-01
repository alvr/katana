package dev.alvr.katana.common.session.data.sources

import arrow.core.Either
import arrow.core.Option
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.flow.Flow

internal interface SessionLocalSource {
    val sessionActive: Flow<Either<Failure, Boolean>>

    suspend fun clearActiveSession(): Either<Failure, Unit>
    suspend fun deleteAnilistToken(): Either<Failure, Unit>
    suspend fun getAnilistToken(): Option<AnilistToken>
    suspend fun logout(): Either<Failure, Unit>
    suspend fun saveSession(anilistToken: AnilistToken): Either<Failure, Unit>
}
