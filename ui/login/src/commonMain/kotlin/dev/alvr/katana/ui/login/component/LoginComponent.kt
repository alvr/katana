package dev.alvr.katana.ui.login.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

sealed interface LoginComponent

fun AppComponentContext.createLoginComponent(): LoginComponent = DefaultLoginComponent(
    componentContext = this,
)
