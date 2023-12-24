package dev.alvr.katana.data.remote.user.sources.info

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.models.UserInfo
import kotlinx.coroutines.flow.Flow

internal interface UserInfoRemoteSource {
    val userInfo: Flow<Either<Failure, UserInfo>>
}
