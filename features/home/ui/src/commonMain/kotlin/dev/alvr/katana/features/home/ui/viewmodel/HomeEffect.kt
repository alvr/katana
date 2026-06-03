package dev.alvr.katana.features.home.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiEffect

internal sealed interface HomeEffect : UiEffect {
    data object SaveTokenFailure : HomeEffect

    data object SaveUserIdFailure : HomeEffect

    data object ObserveSessionFailure : HomeEffect
}
