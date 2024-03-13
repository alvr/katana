package dev.alvr.katana.features.lists.domain.usecases

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository

class UpdateListUseCase(
    dispatcher: KatanaDispatcher,
    private val repository: ListsRepository,
) : EitherUseCase<MediaList, Unit>(dispatcher) {
    override suspend fun run(params: MediaList) = repository.updateList(params)
}
