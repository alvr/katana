package dev.alvr.katana.features.lists.domain.usecases

import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository

class ObserveAnimeListUseCase(
    private val repository: ListsRepository,
) : FlowEitherUseCase<Unit, MediaCollection<MediaEntry.Anime>>() {
    override fun createFlow(params: Unit) = repository.animeCollection
}
