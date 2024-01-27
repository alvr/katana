package dev.alvr.katana.ui.login.viewmodel

internal interface LoginEvent {
    data object LoginSuccessful : LoginEvent
}
