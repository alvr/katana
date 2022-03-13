package dev.alvr.katana.domain.user.repositories

import dev.alvr.katana.domain.user.sources.UserPreferencesSource
import dev.alvr.katana.domain.user.sources.UserRemoteSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val preferences: UserPreferencesSource,
    private val remote: UserRemoteSource,
)
