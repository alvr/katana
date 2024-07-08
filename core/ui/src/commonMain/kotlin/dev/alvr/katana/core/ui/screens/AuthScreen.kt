package dev.alvr.katana.core.ui.screens

import kotlinx.serialization.Serializable

sealed interface AuthScreen : KatanaScreen {
    @Serializable
    data class Login(val token: String? = null) : AuthScreen
}
