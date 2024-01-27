package dev.alvr.katana.ui.login.viewmodel

import dev.alvr.katana.ui.base.decompose.state.UiState

data class LoginState internal constructor(
    internal val saved: Boolean = false,
    internal val loading: Boolean = false,
    internal val errorType: ErrorType? = null,
) : UiState {
    internal enum class ErrorType {
        SaveToken,
        SaveUserId,
    }
}
