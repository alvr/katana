package dev.alvr.katana.ui.home.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

internal class DefaultHomeComponent(
    componentContext: AppComponentContext,
) : HomeComponent, AppComponentContext by componentContext
