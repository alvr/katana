package dev.alvr.katana.domain.user.repositories

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.models.UserInfo

interface UserRepository {
    suspend fun getUserId(): Either<Failure, UserId>
    suspend fun saveUserId(): Either<Failure, Unit>
    suspend fun getUserInfo(): Either<Failure, UserInfo>
}
