package dev.alvr.katana.common.user.data.sources.info

import arrow.core.Either
import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.flow.Flow

internal interface UserInfoRemoteSource {
    val userInfo: Flow<Either<Failure, UserInfo>>
}
