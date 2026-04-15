package dev.alvr.katana.common.user.data.repositories

import dev.alvr.katana.common.user.data.sources.UserLocalSource
import dev.alvr.katana.common.user.data.sources.UserRemoteSource
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Suppress("UnusedPrivateProperty")
internal class UserRepositoryImpl(
    private val localSource: UserLocalSource,
    private val remoteSource: UserRemoteSource,
) : UserRepository {
    override val userInfo = remoteSource.userInfo

    override suspend fun getUserId() = remoteSource.getUserId()

    override suspend fun saveUserId() = remoteSource.saveUserId()
}
