package dev.alvr.katana.ui.lists.component.manga

import dev.alvr.katana.ui.base.decompose.AppComponentContext

sealed interface MangaListComponent

fun AppComponentContext.createMangaListComponent(): MangaListComponent = DefaultMangaListComponent(
    componentContext = this,
)
