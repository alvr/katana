package dev.alvr.katana.domain.session.repositories

import arrow.core.Either
import arrow.core.Option
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.models.AnilistToken
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val sessionActive: Flow<Boolean>

    suspend fun clearActiveSession(): Either<Failure, Unit>
    suspend fun deleteAnilistToken(): Either<Failure, Unit>
    suspend fun getAnilistToken(): Option<AnilistToken>
    suspend fun saveSession(anilistToken: AnilistToken): Either<Failure, Unit>
}
