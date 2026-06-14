package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.ExposeImplBinding
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Stable
@ViewModelKey
@ExposeImplBinding
@ContributesIntoMap(AppScope::class)
internal class ForYouViewModel(
    private val hideWelcomeCardUseCase: HideWelcomeCardUseCase,
    private val observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
) : KatanaViewModel<ForYouState, ForYouEffect, ForYouIntent>(ForYouState()) {

    override fun init() {
        observeWelcomeCardVisibility()
    }

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

    private fun observeWelcomeCardVisibility() {
        execute(
            useCase = observeWelcomeCardVisibilityUseCase,
            params = Unit,
            onFailure = { state { copy(showWelcomeCard = false) } },
            onSuccess = { isVisible -> state { copy(showWelcomeCard = isVisible) } },
        )
    }

    // region [ForYou events]
    private fun handleCloseWelcomeCard() {
        execute(
            useCase = hideWelcomeCardUseCase,
            params = Unit,
            onFailure = { state { copy(showWelcomeCard = false) } },
            onSuccess = { state { copy(showWelcomeCard = false) } },
        )
    }

    private fun handleNavigateToAnimeLists() {
        effect(ForYouEffect.NavigateToAnimeLists)
    }

    private fun handleNavigateToMangaLists() {
        effect(ForYouEffect.NavigateToMangaLists)
    }

    private fun handleNavigateToTrending() {
        effect(ForYouEffect.NavigateToTrending)
    }

    private fun handleNavigateToPopular() {
        effect(ForYouEffect.NavigateToPopular)
    }

    private fun handleNavigateToUpcoming() {
        effect(ForYouEffect.NavigateToUpcoming)
    }
}
