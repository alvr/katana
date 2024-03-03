package dev.alvr.katana.common.user.domain.repositories

import arrow.core.Either
import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userInfo: Flow<Either<Failure, UserInfo>>

    suspend fun getUserId(): Either<Failure, UserId>
    suspend fun saveUserId(): Either<Failure, Unit>
}
