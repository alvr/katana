package dev.alvr.katana.data.remote.user.sources.info

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.models.UserInfo

internal interface UserInfoRemoteSource {
    suspend fun getUserInfo(): Either<Failure, UserInfo>
}
