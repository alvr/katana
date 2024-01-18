package dev.alvr.katana.ui.lists.component.anime

import dev.alvr.katana.ui.base.decompose.AppComponentContext

sealed interface AnimeListComponent

fun AppComponentContext.createAnimeListComponent(): AnimeListComponent = DefaultAnimeListComponent(
    componentContext = this,
)
