package dev.alvr.katana.ui.login

sealed class LoginEffect {
    object Loading : LoginEffect()
    object Saved : LoginEffect()
    object Error : LoginEffect()
}
