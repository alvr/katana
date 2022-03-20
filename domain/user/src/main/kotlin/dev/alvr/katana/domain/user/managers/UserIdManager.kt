package dev.alvr.katana.domain.user.managers

interface UserIdManager {
    suspend fun getId(): Int
}
