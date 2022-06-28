package dev.alvr.katana.ui.login.viewmodel

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
internal data class LoginState(
    val saved: Boolean,
    val loading: Boolean,
    @StringRes val errorMessage: Int?,
) {
    companion object {
        fun initial() = LoginState(
            saved = false,
            loading = false,
            errorMessage = null,
        )
    }
}
