package dev.alvr.katana.features.home.ui.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import co.touchlab.kermit.Logger
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.alvr.katana.features.home.ui.LOGIN_DEEP_LINK_TOKEN

@Stable
internal class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val hideWelcomeCardUseCase: HideWelcomeCardUseCase,
    private val observeActiveSessionUseCase: ObserveActiveSessionUseCase,
    private val observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
) : KatanaViewModel<HomeState, HomeEffect, HomeIntent>(HomeState()) {

    override fun init() {
        observeSession()
        saveAnilistToken()
        loadForYou()
        loadActivity()
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SaveToken -> handleSaveAnilistToken(intent.token)
            is HomeIntent.ForYouIntent -> handleForYouIntent(intent)
            is HomeIntent.ActivityIntent -> handleActivityIntent(intent)
        }
    }

    private fun handleForYouIntent(event: HomeIntent.ForYouIntent) {
        when (event) {
            HomeIntent.ForYouIntent.CloseWelcomeCard -> handleCloseWelcomeCard()
            HomeIntent.ForYouIntent.NavigateToAnimeLists -> handleNavigateToAnimeLists()
            HomeIntent.ForYouIntent.NavigateToMangaLists -> handleNavigateToMangaLists()
            HomeIntent.ForYouIntent.NavigateToTrending -> handleNavigateToTrending()
            HomeIntent.ForYouIntent.NavigateToPopular -> handleNavigateToPopular()
            HomeIntent.ForYouIntent.NavigateToUpcoming -> handleNavigateToUpcoming()
        }
    }

    private fun handleActivityIntent(event: HomeIntent.ActivityIntent) {
        when (event) {
            else -> Unit
        }
    }

    // region [Initialization]
    private fun saveAnilistToken() {
        val token = savedStateHandle.remove<String>(LOGIN_DEEP_LINK_TOKEN) ?: return
        if (token.isBlank()) return

        intent(HomeIntent.SaveToken(token))
    }

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

    private fun loadForYou() {
        observeWelcomeCardVisibility()
    }

    private fun loadActivity() {
        Logger.d(LogTag) { "Loading Activity tab..." }
    }

    // endregion [Initialization]

    // region [ForYou Tab]
    private fun observeWelcomeCardVisibility() {
        execute(
            useCase = observeWelcomeCardVisibilityUseCase,
            params = Unit,
            onFailure = { state { copy(forYouTab = forYouTab.copy(showWelcomeCard = false)) } },
            onSuccess = { isVisible -> state { copy(forYouTab = forYouTab.copy(showWelcomeCard = isVisible)) } },
        )
    }

    // region [ForYou events]
    private fun handleCloseWelcomeCard() {
        execute(
            useCase = hideWelcomeCardUseCase,
            params = Unit,
            onFailure = { state { copy(forYouTab = forYouTab.copy(showWelcomeCard = false)) } },
            onSuccess = { /* no-op */ },
        )
    }

    private fun handleNavigateToAnimeLists() {
        effect(HomeEffect.ForYouEffect.NavigateToAnimeLists)
    }

    private fun handleNavigateToMangaLists() {
        effect(HomeEffect.ForYouEffect.NavigateToMangaLists)
    }

    private fun handleNavigateToTrending() {
        effect(HomeEffect.ForYouEffect.NavigateToTrending)
    }

    private fun handleNavigateToPopular() {
        effect(HomeEffect.ForYouEffect.NavigateToPopular)
    }

    private fun handleNavigateToUpcoming() {
        effect(HomeEffect.ForYouEffect.NavigateToUpcoming)
    }
    // endregion [ForYou events]
    // endregion [ForYou Tab]
}

private const val LogTag = "HomeViewModel"
private const val TokenSeparator = '&'
