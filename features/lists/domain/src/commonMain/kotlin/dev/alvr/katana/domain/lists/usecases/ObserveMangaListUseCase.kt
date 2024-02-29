package dev.alvr.katana.domain.lists.usecases

import dev.alvr.katana.domain.base.usecases.FlowEitherUseCase
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository

class ObserveMangaListUseCase(
    private val repository: ListsRepository,
) : FlowEitherUseCase<Unit, MediaCollection<MediaEntry.Manga>>() {
    override fun createFlow(params: Unit) = repository.mangaCollection
}
