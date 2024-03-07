package dev.alvr.katana.core.ui.decompose

import com.arkivanov.decompose.ComponentContext

@PublishedApi
internal class DefaultAppComponentContext(
    componentContext: ComponentContext,
) : AppComponentContext, ComponentContext by componentContext
