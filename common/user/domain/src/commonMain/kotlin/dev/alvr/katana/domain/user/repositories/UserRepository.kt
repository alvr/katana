package dev.alvr.katana.domain.user.repositories

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.models.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userInfo: Flow<Either<Failure, UserInfo>>

    suspend fun getUserId(): Either<Failure, UserId>
    suspend fun saveUserId(): Either<Failure, Unit>
}
