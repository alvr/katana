package dev.alvr.katana.domain.lists.usecases

import dev.alvr.katana.domain.base.usecases.FlowUseCase
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository

class ObserveAnimeListUseCase(
    private val repository: ListsRepository,
) : FlowUseCase<Unit, MediaCollection<MediaEntry.Anime>>() {
    override fun createFlow(params: Unit) = repository.animeCollection
}
