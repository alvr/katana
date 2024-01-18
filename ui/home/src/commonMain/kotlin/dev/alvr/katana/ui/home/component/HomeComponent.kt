package dev.alvr.katana.ui.home.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

sealed interface HomeComponent

fun AppComponentContext.createHomeComponent(): HomeComponent = DefaultHomeComponent(
    componentContext = this,
)
