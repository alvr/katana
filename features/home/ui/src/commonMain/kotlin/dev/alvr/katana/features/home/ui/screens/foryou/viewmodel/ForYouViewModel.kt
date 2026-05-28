package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Stable
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class ForYouViewModel(
    private val hideWelcomeCardUseCase: HideWelcomeCardUseCase,
    private val observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
) : KatanaViewModel<ForYouState, ForYouEffect, ForYouIntent>(ForYouState()) {

    /**
     * Starts observing welcome-card visibility and updates the view state when visibility changes.
     */
    override fun init() {
        observeWelcomeCardVisibility()
    }

    /**
     * Dispatches the given ForYouIntent to the matching handler.
     *
     * Handles close intent by hiding the welcome card; handles navigation intents by emitting the corresponding navigation effect.
     *
     * @param intent The intent to handle.
     */
    override fun intent(intent: ForYouIntent) {
        when (intent) {
            ForYouIntent.CloseWelcomeCard -> handleCloseWelcomeCard()
            ForYouIntent.NavigateToAnimeLists -> handleNavigateToAnimeLists()
            ForYouIntent.NavigateToMangaLists -> handleNavigateToMangaLists()
            ForYouIntent.NavigateToTrending -> handleNavigateToTrending()
            ForYouIntent.NavigateToPopular -> handleNavigateToPopular()
            ForYouIntent.NavigateToUpcoming -> handleNavigateToUpcoming()
        }
    }

    /**
     * Starts observing whether the welcome card should be shown and updates the view state.
     *
     * On success updates ForYouState.showWelcomeCard to the observed visibility; on failure sets it to false.
     */
    private fun observeWelcomeCardVisibility() {
        execute(
            useCase = observeWelcomeCardVisibilityUseCase,
            params = Unit,
            onFailure = { state { copy(showWelcomeCard = false) } },
            onSuccess = { isVisible -> state { copy(showWelcomeCard = isVisible) } },
        )
    }

    /**
     * Requests hiding of the welcome card and updates the view state to not show it regardless of outcome.
     */
    private fun handleCloseWelcomeCard() {
        execute(
            useCase = hideWelcomeCardUseCase,
            params = Unit,
            onFailure = { state { copy(showWelcomeCard = false) } },
            onSuccess = { state { copy(showWelcomeCard = false) } },
        )
    }

    /**
     * Emits a navigation effect to open the anime lists screen.
     */
    private fun handleNavigateToAnimeLists() {
        effect(ForYouEffect.NavigateToAnimeLists)
    }

    /**
     * Emits a navigation effect to open the manga lists screen.
     */
    private fun handleNavigateToMangaLists() {
        effect(ForYouEffect.NavigateToMangaLists)
    }

    /**
     * Emits a navigation effect requesting transition to the Trending screen.
     */
    private fun handleNavigateToTrending() {
        effect(ForYouEffect.NavigateToTrending)
    }

    /**
     * Emits a navigation effect to open the "Popular" section.
     */
    private fun handleNavigateToPopular() {
        effect(ForYouEffect.NavigateToPopular)
    }

    /**
     * Emits a navigation effect directing the UI to the "Upcoming" screen.
     */
    private fun handleNavigateToUpcoming() {
        effect(ForYouEffect.NavigateToUpcoming)
    }
}
