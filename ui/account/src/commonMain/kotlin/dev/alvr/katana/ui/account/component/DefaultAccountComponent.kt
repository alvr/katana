package dev.alvr.katana.ui.account.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

internal class DefaultAccountComponent(
    componentContext: AppComponentContext,
) : AccountComponent, AppComponentContext by componentContext
