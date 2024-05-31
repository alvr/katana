package dev.alvr.katana.features.home.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiEffect

sealed interface HomeEffect : UiEffect {
    data object ExpiredToken : HomeEffect
}
