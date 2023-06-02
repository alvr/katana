package dev.alvr.katana.data.preferences.session.sources

import androidx.datastore.core.DataStore
import arrow.core.Either
import arrow.core.None
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SessionLocalSourceImpl(
    private val store: DataStore<Session>,
) : SessionLocalSource {
    @Suppress("USELESS_CAST")
    override val sessionActive
        get() = store.data.map { session ->
            (session.anilistToken == null && session.isSessionActive).not().right() as Either<Failure, Boolean>
        }.catch { error ->
            Napier.e(error) { "Error observing the session, setting the as inactive" }
            emit(SessionFailure.CheckingActiveSession.left())
        }

    override suspend fun clearActiveSession() = Either.catch {
        store.updateData { p -> p.copy(isSessionActive = false) }
        Napier.d { "Session cleared" }
    }.mapLeft { error ->
        Napier.e(error) { "Error clearing session" }
        SessionFailure.ClearingSession
    }

    override suspend fun deleteAnilistToken() = Either.catch {
        store.updateData { p -> p.copy(anilistToken = null) }
        Napier.d { "Anilist token deleted" }
    }.mapLeft { error ->
        Napier.e(error) { "Was not possible to delete the token" }
        SessionFailure.DeletingToken
    }

    override suspend fun getAnilistToken() = store.data
        .map { session -> session.anilistToken.toOption() }
        .catch { error ->
            Napier.e(error) { "There was an error reading the token from the preferences" }
            emit(None)
        }.first()

    override suspend fun saveSession(anilistToken: AnilistToken) = Either.catch {
        store.updateData { p -> p.copy(anilistToken = anilistToken, isSessionActive = true) }
        Napier.d { "Token saved: ${anilistToken.token}" }
    }.mapLeft { error ->
        Napier.e(error) { "Was not possible to save the token" }
        SessionFailure.SavingSession
    }
}
