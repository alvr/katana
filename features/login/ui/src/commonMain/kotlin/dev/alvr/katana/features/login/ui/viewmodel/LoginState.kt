package dev.alvr.katana.features.login.ui.viewmodel

internal data class LoginState(
    val saved: Boolean = false,
    val loading: Boolean = false,
    val errorType: ErrorType? = null,
) {
    enum class ErrorType {
        SaveToken,
        SaveUserId,
    }
}
