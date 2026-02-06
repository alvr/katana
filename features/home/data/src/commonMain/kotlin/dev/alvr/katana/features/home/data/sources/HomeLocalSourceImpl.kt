package dev.alvr.katana.features.home.data.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import co.touchlab.kermit.Logger
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.preferences.di.store.KatanaStore
import dev.alvr.katana.features.home.data.entities.HomePreferences
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class HomeLocalSourceImpl(private val store: KatanaStore<HomePreferences>) : HomeLocalSource {
    override val welcomeCardVisible =
        store.data
            .map { homePreferences ->
                @Suppress("USELESS_CAST")
                homePreferences.welcomeCardVisible.right() as Either<Failure, Boolean>
            }
            .catch { error ->
                Logger.e(LogTag, error) { "There was an error getting the visibility of the welcome card" }
                emit(HomeFailure.GettingWelcomeCardVisibility.left())
            }
            .distinctUntilChanged()

    override suspend fun hideWelcomeCard() =
        Either.catch {
                store.update { homePreferences -> homePreferences.copy(welcomeCardVisible = false) }
                Logger.d(LogTag) { "Welcome card hidden" }
            }
            .mapLeft { error ->
                Logger.e(LogTag, error) { "There was an error hiding the welcome card" }
                HomeFailure.HidingWelcomeCard
            }
}

private const val LogTag = "HomeLocalSource"
