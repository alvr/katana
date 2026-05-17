package dev.alvr.katana.features.home.data.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import co.touchlab.kermit.Logger
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.preferences.KatanaPreferenceKey
import dev.alvr.katana.core.preferences.utils.flow
import dev.alvr.katana.core.preferences.utils.set
import dev.alvr.katana.features.home.data.di.HomePreferences
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class HomeLocalSourceImpl(@param:HomePreferences private val safe: KSafe) : HomeLocalSource {
    override val welcomeCardVisible =
        safe
            .flow(WelcomeCardVisiblePrefKey, true)
            .map<_, Either<Failure, Boolean>> { visible -> visible.right() }
            .catch { error ->
                Logger.e(tag = LogTag, throwable = error) {
                    "There was an error getting the visibility of the welcome card"
                }
                emit(HomeFailure.GettingWelcomeCardVisibility.left())
            }
            .distinctUntilChanged()

    override suspend fun hideWelcomeCard() =
        Either.catch {
                safe[WelcomeCardVisiblePrefKey] = false
                Logger.d(tag = LogTag) { "Welcome card hidden" }
            }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "There was an error hiding the welcome card" }
                HomeFailure.HidingWelcomeCard
            }
}

private const val LogTag = "HomeLocalSource"

private data object WelcomeCardVisiblePrefKey : KatanaPreferenceKey<Boolean>
