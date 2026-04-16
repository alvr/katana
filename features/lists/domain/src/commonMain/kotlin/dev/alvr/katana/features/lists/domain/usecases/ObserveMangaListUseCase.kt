package dev.alvr.katana.features.lists.domain.usecases

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.zacsweers.metro.Inject

@Inject
class ObserveMangaListUseCase
internal constructor(dispatcher: KatanaDispatcher, private val repository: ListsRepository) :
    FlowEitherUseCase<Unit, MediaCollection<MediaEntry.Manga>>(dispatcher) {
    override fun createFlow(params: Unit) = repository.mangaCollection
}
