package dev.alvr.katana.data.remote.user.sources

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.models.UserId

internal interface UserRemoteSource {
    suspend fun getUserId(): Either<Failure, UserId>
    suspend fun saveUserId(): Either<Failure, Unit>
}
