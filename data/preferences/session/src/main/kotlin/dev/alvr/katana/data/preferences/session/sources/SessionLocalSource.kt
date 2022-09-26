package dev.alvr.katana.data.preferences.session.sources

import androidx.datastore.core.DataStore
import arrow.core.Either
import arrow.core.None
import arrow.core.toOption
import dev.alvr.katana.data.preferences.base.extensions.handleDataStore
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.failures.SessionPreferencesFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import io.github.aakira.napier.Napier
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SessionLocalSource @Inject constructor(
    private val dataStore: DataStore<Session>,
) {
    val sessionActive get() = dataStore.data.map { session ->
        (session.anilistToken == null && session.isSessionActive).not()
    }

    suspend fun clearActiveSession() = Either.catch(
        f = {
            dataStore.updateData { p -> p.copy(isSessionActive = false) }
            Napier.d { "Session cleared" }
        },
        fe = { error ->
            error.handleDataStore(
                rwException = { SessionPreferencesFailure.ClearingSessionFailure },
                other = { Failure.Unknown },
            )
        },
    )

    suspend fun deleteAnilistToken() = Either.catch(
        f = {
            dataStore.updateData { p -> p.copy(anilistToken = null) }
            Napier.d { "Anilist token deleted" }
        },
        fe = { error ->
            error.handleDataStore(
                rwException = { SessionPreferencesFailure.DeletingTokenFailure },
                other = { Failure.Unknown },
            )
        },
    )

    suspend fun getAnilistToken() = dataStore.data
        .map { token -> token.anilistToken?.let { AnilistToken(it) }.toOption() }
        .catch { emit(None) }
        .first()

    suspend fun saveSession(anilistToken: AnilistToken) = Either.catch(
        f = {
            dataStore.updateData { p -> p.copy(anilistToken = anilistToken.token, isSessionActive = true) }
            Napier.d { "Token saved: ${anilistToken.token}" }
        },
        fe = { error ->
            error.handleDataStore(
                rwException = { SessionPreferencesFailure.SavingFailure },
                other = { Failure.Unknown },
            )
        },
    )
}
