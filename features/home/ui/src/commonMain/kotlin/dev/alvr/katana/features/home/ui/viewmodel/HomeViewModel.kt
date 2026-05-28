package dev.alvr.katana.features.home.ui.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey

@Stable
@AssistedInject
internal class HomeViewModel(
    @Assisted private val token: String?,
    private val observeActiveSessionUseCase: ObserveActiveSessionUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
) : KatanaViewModel<HomeState, HomeEffect, HomeIntent>(HomeState()) {

    /**
     * Starts observing the active session and, if a token was supplied, begins persisting the AniList token.
     *
     * Observation updates the view state with the session activity; token persistence is skipped when the provided token is null or blank.
     */
    override fun init() {
        observeSession()
        saveAnilistToken()
    }

    /**
     * Persists the AniList token supplied to the ViewModel if it is not null or blank.
     *
     * Does nothing when the token is null or blank.
     */
    private fun saveAnilistToken() {
        if (token.isNullOrBlank()) return

        handleSaveAnilistToken(token)
    }

    /**
     * Persists an AniList session token (truncating at the first '&'), emits `HomeEffect.SaveTokenFailure` on failure,
     * and triggers saving the user id on success.
     *
     * @param token The raw AniList token; any content after the first `&` is ignored. 
     */
    private fun handleSaveAnilistToken(token: String) {
        execute(
            useCase = saveSessionUseCase,
            params = AnilistToken(token.substringBefore(TokenSeparator)),
            onFailure = { effect(HomeEffect.SaveTokenFailure) },
            onSuccess = { saveUserId() },
        )
    }

    private fun saveUserId() {
        execute(
            useCase = saveUserIdUseCase,
            params = Unit,
            onFailure = { effect(HomeEffect.SaveUserIdFailure) },
            onSuccess = { /* no-op */ },
        )
    }

    /**
     * Observes the active session status and updates the view state.
     *
     * On each successful update sets `sessionActive` to the observed value.
     * On failure sets `sessionActive` to false and emits `HomeEffect.ObserveSessionFailure`.
     */
    private fun observeSession() {
        execute(
            useCase = observeActiveSessionUseCase,
            params = Unit,
            onFailure = {
                state { copy(sessionActive = false) }
                effect(HomeEffect.ObserveSessionFailure)
            },
            onSuccess = { isActive -> state { copy(sessionActive = isActive) } },
        )
    }

    /**
         * Factory for creating HomeViewModel instances with an optional AniList token.
         *
         * @param token An optional AniList OAuth token (may be null) supplied to the created ViewModel for session persistence.
         * @return A new HomeViewModel configured with the provided token.
         */
        @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted token: String?): HomeViewModel
    }
}

private const val TokenSeparator = '&'
