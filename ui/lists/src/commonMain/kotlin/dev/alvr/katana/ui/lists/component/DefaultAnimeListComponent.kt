package dev.alvr.katana.ui.lists.component

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.lists.entities.item.AnimeListItem
import dev.alvr.katana.ui.lists.viewmodel.AnimeListsViewModel
import org.koin.core.component.inject

internal class DefaultAnimeListComponent(
    componentContext: AppComponentContext,
) : DefaultBaseListComponent<MediaEntry.Anime, AnimeListItem>(componentContext), AnimeListComponent {
    override val viewModel by inject<AnimeListsViewModel>()

    override val state = viewModel.container.stateFlow
}
