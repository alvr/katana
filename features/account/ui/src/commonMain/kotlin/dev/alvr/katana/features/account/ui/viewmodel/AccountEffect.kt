package dev.alvr.katana.features.account.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiEffect

internal sealed interface AccountEffect : UiEffect {
    data object LoggingOutFailure : AccountEffect

    data object LoggingOutSuccess : AccountEffect
}
