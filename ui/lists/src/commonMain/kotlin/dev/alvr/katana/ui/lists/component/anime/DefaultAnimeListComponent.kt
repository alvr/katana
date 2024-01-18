package dev.alvr.katana.ui.lists.component.anime

import dev.alvr.katana.ui.base.decompose.AppComponentContext

internal class DefaultAnimeListComponent(
    componentContext: AppComponentContext,
) : AnimeListComponent, AppComponentContext by componentContext
