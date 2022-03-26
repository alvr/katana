package dev.alvr.katana.domain.token.repositories

import dev.alvr.katana.domain.token.models.AnilistToken

interface TokenPreferencesRepository {
    suspend fun deleteAnilistToken()
    suspend fun getAnilistToken(): AnilistToken?
    suspend fun saveAnilistToken(anilistToken: AnilistToken)
}
