package dev.alvr.katana.features.account.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiIntent

internal sealed interface AccountIntent : UiIntent {
    data object Logout : AccountIntent
}
