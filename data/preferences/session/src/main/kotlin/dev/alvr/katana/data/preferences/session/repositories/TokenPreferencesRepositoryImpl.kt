package dev.alvr.katana.data.preferences.session.repositories

import androidx.datastore.core.DataStore
import arrow.core.Either
import arrow.core.None
import arrow.core.toOption
import dev.alvr.katana.data.preferences.session.models.Token
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.failures.TokenPreferencesFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.TokenPreferencesRepository
import io.github.aakira.napier.Napier
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class TokenPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Token>,
) : TokenPreferencesRepository {
    override suspend fun deleteAnilistToken() = Either.catch(
        f = {
            dataStore.updateData { p -> p.copy(anilistToken = null) }
            Napier.d { "Anilist token deleted" }
        },
        fe = { error ->
            if (error is IOException) {
                TokenPreferencesFailure.DeletingFailure
            } else {
                Failure.Unknown
            }
        },
    )

    override suspend fun getAnilistToken() = dataStore.data
        .map { token -> token.anilistToken?.let { AnilistToken(it) }.toOption() }
        .catch { emit(None) }
        .first()

    override suspend fun saveAnilistToken(anilistToken: AnilistToken) = Either.catch(
        f = {
            dataStore.updateData { p -> p.copy(anilistToken = anilistToken.token) }
            Napier.d { "Token saved: ${anilistToken.token}" }
        },
        fe = { error ->
            if (error is IOException) {
                TokenPreferencesFailure.SavingFailure
            } else {
                Failure.Unknown
            }
        },
    )
}
