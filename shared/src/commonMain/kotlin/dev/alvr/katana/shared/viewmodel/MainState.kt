package dev.alvr.katana.shared.viewmodel

import dev.alvr.katana.core.ui.destinations.RootDestination
import dev.alvr.katana.core.ui.viewmodel.UiState

internal data class MainState(val initialScreen: RootDestination) : UiState
