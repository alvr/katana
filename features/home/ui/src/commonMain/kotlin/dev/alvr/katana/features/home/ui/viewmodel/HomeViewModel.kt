package dev.alvr.katana.features.home.ui.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.core.ui.navigation.DeepLinkState
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.launch

@Stable
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class HomeViewModel(
    private val deepLinkState: DeepLinkState,
    private val observeActiveSessionUseCase: ObserveActiveSessionUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
) : KatanaViewModel<HomeState, HomeEffect, HomeIntent>(HomeState()) {

    override fun init() {
        observeSession()
        saveAnilistToken()
    }

    private fun saveAnilistToken() {
        viewModelScope.launch {
            deepLinkState.token.collect { token ->
                if (!token.isNullOrBlank()) {
                    handleSaveAnilistToken(token)
                    deepLinkState.consumeToken()
                }
            }
        }
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
}

private const val TokenSeparator = '&'
