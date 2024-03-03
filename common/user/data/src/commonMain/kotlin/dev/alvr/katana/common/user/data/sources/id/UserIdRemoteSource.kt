package dev.alvr.katana.common.user.data.sources.id

import arrow.core.Either
import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.core.domain.failures.Failure

internal interface UserIdRemoteSource {
    suspend fun getUserId(): Either<Failure, UserId>
    suspend fun saveUserId(): Either<Failure, Unit>
}
