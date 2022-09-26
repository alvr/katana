package dev.alvr.katana.data.remote.user.repositories

import dev.alvr.katana.data.remote.user.sources.UserRemoteSource
import dev.alvr.katana.domain.user.repositories.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val source: UserRemoteSource,
) : UserRepository {
    override suspend fun getUserId() = source.getUserId()
    override suspend fun saveUserId() = source.saveUserId()
}
