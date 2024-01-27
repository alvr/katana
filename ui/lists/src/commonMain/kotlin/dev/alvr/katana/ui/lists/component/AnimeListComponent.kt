 package dev.alvr.katana.ui.lists.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.lists.entities.item.AnimeListItem

 sealed interface AnimeListComponent : BaseListComponent<AnimeListItem>

 fun AppComponentContext.createAnimeListComponent(): AnimeListComponent = DefaultAnimeListComponent(
     componentContext = this,
 )
