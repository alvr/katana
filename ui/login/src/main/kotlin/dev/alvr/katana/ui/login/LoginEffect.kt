package dev.alvr.katana.ui.login

internal sealed class LoginEffect {
    object Loading : LoginEffect()
    object Saved : LoginEffect()
    object Error : LoginEffect()
}
