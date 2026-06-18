package dev.alvr.katana.features.lists.domain.usecases

import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface UpdateListUseCase : KatanaEitherUseCase<MediaList, Unit>

@ContributesBinding(AppScope::class, binding = binding<UpdateListUseCase>())
internal class UpdateListUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: ListsRepository) :
    EitherUseCase<MediaList, Unit>(dispatcher), UpdateListUseCase {
    override suspend fun run(params: MediaList) = repository.updateList(params)
}
