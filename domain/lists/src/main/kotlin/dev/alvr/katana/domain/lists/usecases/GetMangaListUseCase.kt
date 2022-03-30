package dev.alvr.katana.domain.lists.usecases

import dev.alvr.katana.domain.base.FlowUseCase
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MangaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import javax.inject.Inject

class GetMangaListUseCase @Inject constructor(
    private val repository: ListsRepository
) : FlowUseCase<Unit, MediaCollection<MangaEntry>>() {
    override fun createFlow(params: Unit) = repository.mangaList
}
