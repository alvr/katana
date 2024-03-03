package dev.alvr.katana.common.user.data.repositories

import dev.alvr.katana.common.user.data.sources.id.UserIdRemoteSource
import dev.alvr.katana.common.user.data.sources.info.UserInfoRemoteSource
import dev.alvr.katana.common.user.domain.repositories.UserRepository

internal class UserRepositoryImpl(
    private val idSource: UserIdRemoteSource,
    private val infoSource: UserInfoRemoteSource,
) : UserRepository {
    override val userInfo get() = infoSource.userInfo

    override suspend fun getUserId() = idSource.getUserId()
    override suspend fun saveUserId() = idSource.saveUserId()
}
