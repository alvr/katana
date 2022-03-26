package dev.alvr.katana.data.preferences.token.repositories

import androidx.datastore.core.DataStore
import dev.alvr.katana.data.preferences.token.models.Token
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import io.github.aakira.napier.Napier
import javax.inject.Inject
import kotlinx.coroutines.flow.first

internal class TokenPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Token>
) : TokenPreferencesRepository {
    private var token: AnilistToken? = null

    override suspend fun deleteAnilistToken() {
        Napier.d { "Deleting Anilist token" }
        dataStore.updateData { p -> p.copy(anilistToken = null) }
    }

    override suspend fun getAnilistToken(): AnilistToken? =
        token ?: dataStore.data.first().anilistToken?.let { token -> AnilistToken(token) }?.also {
            token = it
        }

    override suspend fun saveAnilistToken(anilistToken: AnilistToken) {
        Napier.d { "Token saved: ${anilistToken.token}" }
        dataStore.updateData { p -> p.copy(anilistToken = anilistToken.token) }
    }
}
