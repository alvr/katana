package dev.alvr.katana.common.user.data.repositories

import dev.alvr.katana.common.user.data.sources.UserLocalSource
import dev.alvr.katana.common.user.data.sources.UserRemoteSource
import dev.alvr.katana.common.user.domain.repositories.UserRepository

@Suppress("UnusedPrivateProperty")
internal class UserRepositoryImpl(
    @Suppress("Unused") private val localSource: UserLocalSource,
    private val remoteSource: UserRemoteSource,
) : UserRepository {
    override val userInfo = remoteSource.userInfo

    override suspend fun getUserId() = remoteSource.getUserId()

    override suspend fun saveUserId() = remoteSource.saveUserId()
}
