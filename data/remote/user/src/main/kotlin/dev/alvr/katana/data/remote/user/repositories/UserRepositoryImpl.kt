package dev.alvr.katana.data.remote.user.repositories

import dev.alvr.katana.data.remote.user.sources.UserRemoteSource
import dev.alvr.katana.domain.user.repositories.UserRepository

internal class UserRepositoryImpl(
    private val source: UserRemoteSource,
) : UserRepository {
    override suspend fun getUserId() = source.getUserId()
    override suspend fun saveUserId() = source.saveUserId()
}
