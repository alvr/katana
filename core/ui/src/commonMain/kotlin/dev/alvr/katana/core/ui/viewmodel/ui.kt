package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable interface UiEffect

@Stable interface UiIntent

@Immutable interface UiState

data object EmptyState : UiState

data object EmptyEffect : UiEffect

data object EmptyIntent : UiIntent
