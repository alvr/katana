package dev.alvr.katana.common.session.data.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import co.touchlab.kermit.Logger
import dev.alvr.katana.common.session.data.di.SessionPreferences
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.preferences.KatanaPreferenceKey
import dev.alvr.katana.core.preferences.utils.flow
import dev.alvr.katana.core.preferences.utils.get
import dev.alvr.katana.core.preferences.utils.set
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class SessionLocalSourceImpl(@param:SessionPreferences private val safe: KSafe) : SessionLocalSource {
    override val sessionActive =
        safe
            .flow(SessionActivePrefKey, false)
            .combine(safe.flow(AnilistTokenPrefKey, null)) { sessionActive, anilistToken ->
                (anilistToken != null && sessionActive).right() as Either<Failure, Boolean>
            }
            .catch { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error observing the session" }
                emit(SessionFailure.CheckingActiveSession.left())
            }
            .distinctUntilChanged()

    override suspend fun clearActiveSession() =
        Either.catch {
                safe[SessionActivePrefKey] = false
                Logger.d(tag = LogTag) { "Session cleared" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error clearing session" }
                SessionFailure.ClearingSession
            }

    override suspend fun deleteAnilistToken() =
        Either.catch {
                safe[AnilistTokenPrefKey] = null
                Logger.d(tag = LogTag) { "Anilist token deleted" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error deleting the token" }
                SessionFailure.DeletingToken
            }

    override suspend fun getAnilistToken() =
        safe[AnilistTokenPrefKey, null].toOption().map { token -> AnilistToken(token) }

    override suspend fun logout() =
        Either.catch {
                safe[AnilistTokenPrefKey] = null
                safe[SessionActivePrefKey] = false
                Logger.d(tag = LogTag) { "Logged out" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error logging out" }
                SessionFailure.LoggingOut
            }

    override suspend fun saveSession(anilistToken: AnilistToken) =
        Either.catch {
                safe[AnilistTokenPrefKey] = anilistToken.token
                safe[SessionActivePrefKey] = true
                Logger.d(tag = LogTag) { "Token saved successfully" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error saving the token" }
                SessionFailure.SavingSession
            }
}

private const val LogTag = "SessionLocalSource"

private data object AnilistTokenPrefKey : KatanaPreferenceKey<String?>

private data object SessionActivePrefKey : KatanaPreferenceKey<Boolean>
