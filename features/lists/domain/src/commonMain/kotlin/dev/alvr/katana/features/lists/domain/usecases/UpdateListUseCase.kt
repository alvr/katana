package dev.alvr.katana.features.lists.domain.usecases

import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository

class UpdateListUseCase(
    private val repository: ListsRepository,
) : EitherUseCase<MediaList, Unit> {
    override suspend fun invoke(entry: MediaList) = repository.updateList(entry)
}
