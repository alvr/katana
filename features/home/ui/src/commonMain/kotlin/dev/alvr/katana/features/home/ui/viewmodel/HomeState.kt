package dev.alvr.katana.features.home.ui.viewmodel

import androidx.compose.runtime.Immutable
import dev.alvr.katana.core.ui.viewmodel.UiState

@Immutable
internal data class HomeState(
    val sessionActive: Boolean = false,
    val forYouTab: ForYouTabState = ForYouTabState(),
    val activityTab: ActivityTabState = ActivityTabState,
) : UiState {

    @Immutable data class ForYouTabState(val showWelcomeCard: Boolean = false) : UiState

    @Immutable data object ActivityTabState : UiState
}
