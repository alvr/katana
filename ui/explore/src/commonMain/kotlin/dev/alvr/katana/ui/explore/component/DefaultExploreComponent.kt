package dev.alvr.katana.ui.explore.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

internal class DefaultExploreComponent(
    componentContext: AppComponentContext,
) : ExploreComponent, AppComponentContext by componentContext
