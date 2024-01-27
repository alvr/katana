package dev.alvr.katana.ui.account.component

import dev.alvr.katana.ui.account.viewmodel.AccountState
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.state.StatefulComponent

sealed interface AccountComponent : StatefulComponent<AccountState> {
    val navigateToLogin: () -> Unit

    fun onLogoutClick()
}

fun AppComponentContext.createAccountComponent(
    navigateToLogin: () -> Unit,
): AccountComponent = DefaultAccountComponent(
    componentContext = this,
    navigateToLogin = navigateToLogin,
)
