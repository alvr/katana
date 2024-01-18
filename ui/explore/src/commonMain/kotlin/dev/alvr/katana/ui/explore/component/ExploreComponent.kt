package dev.alvr.katana.ui.explore.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

sealed interface ExploreComponent

fun AppComponentContext.createExploreComponent(): ExploreComponent = DefaultExploreComponent(
    componentContext = this,
)
