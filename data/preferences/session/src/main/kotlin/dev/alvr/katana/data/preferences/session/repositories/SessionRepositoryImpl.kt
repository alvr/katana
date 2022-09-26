package dev.alvr.katana.data.preferences.session.repositories

import dev.alvr.katana.data.preferences.session.sources.SessionLocalSource
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository
import javax.inject.Inject

internal class SessionRepositoryImpl @Inject constructor(
    private val source: SessionLocalSource,
) : SessionRepository {
    override val sessionActive get() = source.sessionActive
    override suspend fun clearActiveSession() = source.clearActiveSession()
    override suspend fun deleteAnilistToken() = source.deleteAnilistToken()
    override suspend fun getAnilistToken() = source.getAnilistToken()
    override suspend fun saveSession(anilistToken: AnilistToken) = source.saveSession(anilistToken)
}
