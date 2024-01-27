package dev.alvr.katana.ui.lists.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.lists.entities.item.MangaListItem

sealed interface MangaListComponent : BaseListComponent<MangaListItem>

fun AppComponentContext.createMangaListComponent(): MangaListComponent = DefaultMangaListComponent(
    componentContext = this,
)
