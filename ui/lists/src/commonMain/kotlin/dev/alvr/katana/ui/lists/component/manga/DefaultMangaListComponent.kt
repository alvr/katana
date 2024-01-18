package dev.alvr.katana.ui.lists.component.manga

import dev.alvr.katana.ui.base.decompose.AppComponentContext

internal class DefaultMangaListComponent(
    componentContext: AppComponentContext,
) : MangaListComponent, AppComponentContext by componentContext
