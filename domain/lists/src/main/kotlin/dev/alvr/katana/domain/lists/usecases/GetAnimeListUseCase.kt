package dev.alvr.katana.domain.lists.usecases

import dev.alvr.katana.domain.base.FlowUseCase
import dev.alvr.katana.domain.lists.models.AnimeEntry
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import javax.inject.Inject

class GetAnimeListUseCase @Inject constructor(
    private val repository: ListsRepository
) : FlowUseCase<Unit, List<MediaCollection<AnimeEntry>>>() {
    override fun createFlow(params: Unit) = repository.animeList
}
