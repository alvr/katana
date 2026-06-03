package dev.alvr.katana.features.home.ui.viewmodel

import androidx.compose.runtime.Immutable
import dev.alvr.katana.core.ui.viewmodel.UiState

@Immutable internal data class HomeState(val sessionActive: Boolean = false) : UiState
