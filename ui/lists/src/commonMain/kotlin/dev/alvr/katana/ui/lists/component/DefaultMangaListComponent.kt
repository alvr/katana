package dev.alvr.katana.ui.lists.component

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.lists.entities.item.MangaListItem
import dev.alvr.katana.ui.lists.viewmodel.MangaListsViewModel
import org.koin.core.component.inject

internal class DefaultMangaListComponent(
    componentContext: AppComponentContext,
) : DefaultBaseListComponent<MediaEntry.Manga, MangaListItem>(componentContext), MangaListComponent {
    override val viewModel by inject<MangaListsViewModel>()

    override val state = viewModel.container.stateFlow
}
