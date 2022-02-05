package dev.alvr.katana.ui.login

sealed class LoginEffect {
    object Saved : LoginEffect()
    object Error : LoginEffect()
}
