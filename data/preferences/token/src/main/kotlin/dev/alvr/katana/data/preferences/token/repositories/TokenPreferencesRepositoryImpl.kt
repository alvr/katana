package dev.alvr.katana.data.preferences.token.repositories

import androidx.datastore.core.DataStore
import dev.alvr.katana.data.preferences.token.models.Token
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

internal class TokenPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Token>
) : TokenPreferencesRepository {
    override suspend fun getAnilistToken(): AnilistToken? =
        dataStore.data.first().anilistToken?.let { token -> AnilistToken(token) }

    override suspend fun saveAnilistToken(anilistToken: AnilistToken) {
        dataStore.updateData { p -> p.copy(anilistToken = anilistToken.token) }
    }
}
