package dev.alvr.katana.domain.user.repositories

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.models.UserId

interface UserRemoteRepository {
    suspend fun getUserId(): Either<Failure, UserId>
    suspend fun saveUserId(): Either<Failure, Unit>
}
