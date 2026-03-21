package dev.alvr.katana.common.session.data.sources

import arrow.core.Either
import arrow.core.None
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import co.touchlab.kermit.Logger
import dev.alvr.katana.common.session.data.entities.Session
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.preferences.di.store.KatanaStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SessionLocalSourceImpl(private val store: KatanaStore<Session>) : SessionLocalSource {
    override val sessionActive =
        store.data
            .map { session ->
                @Suppress("USELESS_CAST")
                (session.anilistToken != null && session.sessionActive).right() as Either<Failure, Boolean>
            }
            .catch { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error observing the session" }
                emit(SessionFailure.CheckingActiveSession.left())
            }
            .distinctUntilChanged()

    override suspend fun clearActiveSession() =
        Either.catch {
                store.update { p -> p.copy(sessionActive = false) }
                Logger.d(tag = LogTag) { "Session cleared" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error clearing session" }
                SessionFailure.ClearingSession
            }

    override suspend fun deleteAnilistToken() =
        Either.catch {
                store.update { p -> p.copy(anilistToken = null) }
                Logger.d(tag = LogTag) { "Anilist token deleted" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error deleting the token" }
                SessionFailure.DeletingToken
            }

    override suspend fun getAnilistToken() =
        store.data
            .map { session -> session.anilistToken.toOption() }
            .catch { error ->
                Logger.e(tag = LogTag, throwable = error) {
                    "There was an error reading the token from the preferences"
                }
                emit(None)
            }
            .first()

    override suspend fun logout() =
        Either.catch {
                store.update { p -> p.copy(anilistToken = null, sessionActive = false) }
                Logger.d(tag = LogTag) { "Logged out" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error logging out" }
                SessionFailure.LoggingOut
            }

    override suspend fun saveSession(anilistToken: AnilistToken) =
        Either.catch {
                store.update { p -> p.copy(anilistToken = anilistToken, sessionActive = true) }
                Logger.d(tag = LogTag) { "Token saved: ${anilistToken.token}" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error saving the token" }
                SessionFailure.SavingSession
            }
}

private const val LogTag = "SessionLocalSource"
