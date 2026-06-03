package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import androidx.compose.runtime.Immutable
import dev.alvr.katana.core.ui.viewmodel.UiState

@Immutable internal data class ForYouState(val showWelcomeCard: Boolean = false) : UiState
