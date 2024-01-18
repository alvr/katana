package dev.alvr.katana.ui.account.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

sealed interface AccountComponent

fun AppComponentContext.createAccountComponent(): AccountComponent = DefaultAccountComponent(
    componentContext = this,
)
