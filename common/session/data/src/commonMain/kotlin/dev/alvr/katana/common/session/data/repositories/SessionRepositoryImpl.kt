package dev.alvr.katana.common.session.data.repositories

import dev.alvr.katana.common.session.data.sources.SessionLocalSource
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.repositories.SessionRepository

internal class SessionRepositoryImpl(
    private val source: SessionLocalSource,
) : SessionRepository {
    override val sessionActive get() = source.sessionActive

    override suspend fun clearActiveSession() = source.clearActiveSession()
    override suspend fun deleteAnilistToken() = source.deleteAnilistToken()
    override suspend fun getAnilistToken() = source.getAnilistToken()
    override suspend fun logout() = source.logout()
    override suspend fun saveSession(anilistToken: AnilistToken) = source.saveSession(anilistToken)
}
