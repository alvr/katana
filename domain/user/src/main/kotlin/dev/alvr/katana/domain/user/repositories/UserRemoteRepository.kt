package dev.alvr.katana.domain.user.repositories

import dev.alvr.katana.domain.user.models.UserId

interface UserRemoteRepository {
    suspend fun getUserId(): UserId
    suspend fun saveUserId()
}
