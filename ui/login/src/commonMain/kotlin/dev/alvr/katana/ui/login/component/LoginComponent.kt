package dev.alvr.katana.ui.login.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.state.StatefulComponent
import dev.alvr.katana.ui.login.viewmodel.LoginState

sealed interface LoginComponent : StatefulComponent<LoginState> {
    val token: String?
    val navigateToHome: () -> Unit
}

fun AppComponentContext.createLoginComponent(
    token: String?,
    navigateToHome: () -> Unit,
): LoginComponent = DefaultLoginComponent(
    componentContext = this,
    token = token,
    navigateToHome = navigateToHome,
)
