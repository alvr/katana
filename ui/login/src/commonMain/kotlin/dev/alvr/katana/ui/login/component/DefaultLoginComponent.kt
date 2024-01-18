package dev.alvr.katana.ui.login.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

internal class DefaultLoginComponent(
    componentContext: AppComponentContext,
) : LoginComponent, AppComponentContext by componentContext
