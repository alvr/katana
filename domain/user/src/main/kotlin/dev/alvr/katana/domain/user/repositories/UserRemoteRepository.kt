package dev.alvr.katana.domain.user.repositories

interface UserRemoteRepository {
    suspend fun saveUserId()
}
