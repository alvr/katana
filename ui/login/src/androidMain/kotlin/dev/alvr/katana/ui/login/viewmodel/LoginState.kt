package dev.alvr.katana.ui.login.viewmodel

import androidx.annotation.StringRes

internal data class LoginState(
    val saved: Boolean = false,
    val loading: Boolean = false,
    @StringRes val errorMessage: Int? = null,
)
