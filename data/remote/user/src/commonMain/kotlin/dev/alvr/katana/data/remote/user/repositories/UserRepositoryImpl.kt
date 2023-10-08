package dev.alvr.katana.data.remote.user.repositories

import dev.alvr.katana.data.remote.user.sources.id.UserIdRemoteSource
import dev.alvr.katana.data.remote.user.sources.info.UserInfoRemoteSource
import dev.alvr.katana.domain.user.repositories.UserRepository

internal class UserRepositoryImpl(
    private val idSource: UserIdRemoteSource,
    private val infoSource: UserInfoRemoteSource,
) : UserRepository {
    override suspend fun getUserId() = idSource.getUserId()
    override suspend fun saveUserId() = idSource.saveUserId()

    override suspend fun getUserInfo() = infoSource.getUserInfo()
}
