package dev.alvr.katana.domain.token.repositories

import dev.alvr.katana.domain.token.models.AnilistToken

interface TokenPreferencesRepository {
    suspend fun getAnilistToken(): AnilistToken?
    suspend fun saveAnilistToken(anilistToken: AnilistToken)
}
