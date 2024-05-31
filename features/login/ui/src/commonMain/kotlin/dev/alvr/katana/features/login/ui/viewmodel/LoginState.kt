package dev.alvr.katana.features.login.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiState

internal data class LoginState(
    val saved: Boolean = false,
    val loading: Boolean = false,
    val errorType: ErrorType? = null,
) : UiState {
    enum class ErrorType {
        SaveToken,
        SaveUserId,
    }
}
